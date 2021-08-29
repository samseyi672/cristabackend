package com.crista.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.crista.enties.ServerMessages;
import com.crista.repositories.ServerMessageRepository;

@RestController
public class NewsController {

	public List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
	
	@Autowired
	ServerMessageRepository serverMessage ;

	public Map<String, SseEmitter> emitEventToSpecificUser = new ConcurrentHashMap<String, SseEmitter>();

	// method for client subscription
	@RequestMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE)
	public SseEmitter subscribe(@RequestParam(value = "userid", required = false) String userid,
			@RequestParam(value = "vendorname", required = false) String vendorname) throws IOException {
		SseEmitter sse = new SseEmitter();
		if(userid != null) {
			//SseEmitter sse = new SseEmitter();
			//System.out.println("");
			sse.send(SseEmitter.event().name("INIT"));
			sse.onCompletion(() -> {
				emitters.remove(sse);
			});
			sse.onTimeout(() -> {
				emitters.remove(sse);
			});
			sse.onError((e) -> {
				emitters.remove(sse);
			});
			if (userid != null) {
				System.out.println("added to user id");
				emitEventToSpecificUser.put(userid, sse);
			}
			return sse;	
		}else if(vendorname !=null) {
			//SseEmitter sse = new SseEmitter();
			sse.send(SseEmitter.event().name("INIT"));
			System.out.println("open connection on init3");
			sse.onCompletion(() -> {
				emitters.remove(sse);
			});
			sse.onTimeout(() -> {
				emitters.remove(sse);
			});
			sse.onError((e) -> {
				emitters.remove(sse);
			});
			if (vendorname != null) {
				System.out.println("added to vendor ");
				emitEventToSpecificUser.put(vendorname, sse);
			}
			return sse;	
		}
		sse.send(SseEmitter.event().name("INIT"));
		System.out.println("open connection on init");
		sse.onCompletion(() -> {
			emitters.remove(sse);
		});
		emitters.add(sse);
		return sse;
	}

	// for specific client
	// i didnt use this 
	@RequestMapping(value = "/eventbyuser", consumes = MediaType.ALL_VALUE)
	public SseEmitter byuserEvent(@RequestParam(value = "userid", required = false) String userid) throws IOException {
		SseEmitter sse = new SseEmitter();
		System.out.println("calling event by userid");
		sse.send(SseEmitter.event().name("INIT2"));
		System.out.println("open connection on init2");
		sse.onCompletion(() -> {
			emitters.remove(sse);
		});
		sse.onTimeout(() -> {
			emitters.remove(sse);
		});
		sse.onError((e) -> {
			emitters.remove(sse);
		});
		if (userid != null) {
			System.out.println("added to user id");
			emitEventToSpecificUser.put(userid, sse);
		}
		return sse;
	}

	  // i didnt use this
	@RequestMapping(value = "/eventbyvendorname", consumes = MediaType.ALL_VALUE)
	public SseEmitter byVendorName(@RequestParam(value = "vendorname", required = false) String vendorname)
			throws IOException {
		SseEmitter sse = new SseEmitter();
		sse.send(SseEmitter.event().name("INIT2"));
		sse.onCompletion(() -> {
			emitters.remove(sse);
		});
		sse.onTimeout(() -> {
			emitters.remove(sse);
		});
		sse.onError((e) -> {
			emitters.remove(sse);
		});
		if (vendorname != null) {
			System.out.println("added to vendor ");
			emitEventToSpecificUser.put(vendorname, sse);
		}
		return sse;
	}

	  // dispatch event at real time
	   @PostMapping(value = "/dispatchEvent")
	public void dispatchEventToClients(@RequestParam(value="freshNews",required = false) String freshNews,
			@RequestParam(value = "userid", required = false) String userid[],
			@RequestParam(value = "vendorname", required = false) String vendorname[],
			@RequestParam(value = "alluserorVendorname", required = false) String alluserorVendorname)
			throws IOException {
		System.out.println("fresh news " + freshNews);
		System.out.println("userid news " + userid);
		System.out.println("vendorname news " + vendorname);
		System.out.println("alluserorVendorname news " + alluserorVendorname);
//		System.exit(0);
		// to user by id
		if(userid != null && vendorname != null) {
		if (userid.length != 0) {
			for (String id : userid) {
				System.out.println("sending user news");
				System.out.println("events "+ emitEventToSpecificUser.get(id));
				emitEventToSpecificUser.get(id).send(SseEmitter.event().name("usernews").data(freshNews));
				System.out.println("sent news ");
				ServerMessages msg  = new ServerMessages() ;
				 msg.setMessage(freshNews);
				 msg.setUserid(id);
				 msg.setVendorname(null);
				 msg.setView("unview");
				serverMessage.save(msg);
				emitEventToSpecificUser.remove(id) ;
				// then save to db for user
			}
			return  ;
		}
		// to user by vendor name
		if (vendorname.length != 0) {
			for (String vendor : vendorname) {
				emitEventToSpecificUser.get(vendor).send(SseEmitter.event().name("vendornews").data(freshNews));
				ServerMessages msg  = new ServerMessages() ;
				 msg.setMessage(freshNews);
				 msg.setUserid(null);
				 msg.setVendorname(vendor);
				 msg.setView("unview");
				serverMessage.save(msg);
				emitEventToSpecificUser.remove(vendor) ;
				// then save to db for vendor
			}
			return ;
		}
		}
		// send to all users
		for (SseEmitter sse : emitters) {
			try {			
					System.out.println("sending latest News ");
					sse.send(SseEmitter.event().name("latestNews").data(freshNews));
					 System.out.println("sent latest news ");
					ServerMessages msg  = new ServerMessages() ;
					 msg.setMessage(freshNews);
					// msg.setUserid(id);
					// msg.setVendorname(null);
					 msg.setView("unview");
					serverMessage.save(msg);
					// then save to db for all user
			} catch (IOException e) {
				emitters.remove(sse);
			}
		}
	}
}


















































































