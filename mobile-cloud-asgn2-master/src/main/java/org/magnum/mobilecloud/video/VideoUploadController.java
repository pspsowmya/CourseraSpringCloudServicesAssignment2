package org.magnum.mobilecloud.video;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VideoUploadController {

	private static List<Video> videoList = new ArrayList<>();

	@Autowired
	VideoRepository repository;

	//Method to validate oauth authentication
	@RequestMapping(value="/validateUser" , method=RequestMethod.GET)
	public @ResponseBody Principal user(Principal user, HttpServletResponse response) {
		System.out.println(user);	
		return user;
	}

	// Method to add a video using Spring Data JPA
	@RequestMapping(value="/video", method=RequestMethod.POST)
	public Video addVideoMetadata(@RequestBody Video v,HttpServletResponse response, Principal user) throws IOException {
		Video video = repository.save(v);
		return video;
	}

	//Method to retrieve list of videos from Spring Data JPA
	@RequestMapping(value="/video", method=RequestMethod.GET)
	public @ResponseBody
	List<Video> addGetVideo(HttpServletResponse response, Principal user) throws IOException {
		if(response.getStatus() != 200) {
			response.sendError(response.getStatus());
		}
		videoList = (List<Video>) repository.findAll();
		return videoList;
	}

	//Method to retrieve a specified video from Spring Data JPA
	@RequestMapping(value="/video/{id}", method=RequestMethod.GET)
	public @ResponseBody
	Optional<Video> getVideoById(HttpServletResponse response, @PathVariable("id") long id, Principal user) throws IOException {   
		if(response.getStatus() != 200) {
			response.sendError(response.getStatus());
		}
		Optional<Video> video = repository.findById(id);
		return video;
	}

	//Method to find a video by name
	@RequestMapping(value="/video/search/findByName", method=RequestMethod.GET)
	public @ResponseBody
	List<Video> getVideoByTitle(HttpServletResponse response, @RequestParam(name = "title") String title, Principal user) throws IOException {   
		if(response.getStatus() != 200) {
			response.sendError(response.getStatus());
		}
		return repository.findByName(title);
	}

	//Method to find a video based on duration
	@RequestMapping(value="/video/search/findByDurationLessThan", method=RequestMethod.GET)
	public @ResponseBody
	List<Video> getVideoByDurationLessThan(HttpServletResponse response, @RequestParam(name = "duration") Long duration, Principal user) throws IOException {   
		if(response.getStatus() != 200) {
			response.sendError(response.getStatus());
		}
		return repository.findByDurationLessThan(duration);
	}


	//Method to like an existing video
	@RequestMapping(value="/video/{id}/like", method=RequestMethod.POST)
	public ResponseEntity<HttpStatus> updateLikedVideo(HttpServletResponse response, @PathVariable("id") Long id,Principal user) throws IOException {   
		Video found = repository.findById(id).orElse(null);
		if(found == null) {
			return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
		}
		else {
			if (found.getLikedBy().contains(user.getName())) {
				return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
			} else {
				found.getLikedBy().add(user.getName());
				found.setLikedBy(found.getLikedBy());
				found.setLikes(found.getLikes() + 1);
				repository.save(found);
				return new ResponseEntity<HttpStatus>(HttpStatus.OK);
			}
		}
	}
	
	//Method to unlike an already liked video
	@RequestMapping(value="/video/{id}/unlike", method=RequestMethod.POST)
	public ResponseEntity<HttpStatus> updateUnlikedVideo(HttpServletResponse response, @PathVariable("id") Long id,Principal user) throws IOException {   
		Video found = repository.findById(id).orElse(null);
		if(found == null) {
			return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
		}
		else {
			if (found.getLikedBy().contains(user.getName())) {
				found.setLikes(found.getLikes()-1);
				repository.save(found);
			} else {
				return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		}
		}
	
}
