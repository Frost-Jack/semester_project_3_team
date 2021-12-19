package com.example.accessingdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

@Controller
@RequestMapping(path="/demo")
public class MainController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LabRepository labRepository;

	@PostMapping(path="/add")
	public @ResponseBody String addNewUser (
			@RequestParam String login
			, @RequestParam String pass
			, @RequestParam String stat
			, @RequestParam String firstName
			, @RequestParam String lastName
	) {

		User n = new User();
		n.setLogin(login);
		n.setPass(n.getHashedPass(pass));
		n.setStat(stat);
		n.setFirstName(firstName);
		n.setLastName(lastName);
		userRepository.save(n);
		return "Saved";
	}

	@GetMapping(path="/all")
	public @ResponseBody Iterable<LabTerms> getAllLabs() {
		return labRepository.findAll();
	}

	@GetMapping(path="/labs/group")
	public @ResponseBody String getLabsByGroup(
			@RequestParam String login,
			@RequestParam String pass,
			HttpServletRequest request
	) {
		List<User> users = userRepository.findByLogin(login);

		User user = users.stream()
				.filter(usr -> usr.getHashedPass(pass).equals(usr.getPass()))
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with login=" + login));

		String gr = user.getStat();
		List<LabTerms> labs = labRepository.findByStat();
		if(gr=="-1") {
			for (LabTerms element : labs) {
				return String.format("Lab%s: %s(%s) - max points:%s deadline:%s \n", element.getId(), element.getLab_name(), element.getSubj(), element.getMax_points(), element.getDeadline());
			}
		}
		else{
			for (LabTerms element : labs) {
				if(element.getGroup_nums().contains(gr))
					return String.format("Lab%s: %s(%s) - max points:%s deadline:%s \n", element.getId(), element.getLab_name(), element.getSubj(), element.getMax_points(), element.getDeadline());
			}
		}
		return "\n";
	}

	@GetMapping(path="/labs/subj")
	public @ResponseBody String getLabsByClass(
			@RequestParam String login,
			@RequestParam String pass,
			HttpServletRequest request
	) {
		List<User> users = userRepository.findByLogin(login);

		User user = users.stream()
				.filter(usr -> usr.getHashedPass(pass).equals(usr.getPass()))
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with login=" + login));

		String gr = user.getStat();
		List<LabTerms> labs = labRepository.findByStat();
		if(gr=="-1") {
			for (LabTerms element : labs) {
				String sj = element.getSubj();
				if(element.getSubj().contains(sj))
					return String.format("Lab%s: %s(%s) - max points:%s deadline:%s \n", element.getId(), element.getLab_name(), element.getSubj(), element.getMax_points(), element.getDeadline());
			}
		}
		else{
			for (LabTerms element : labs) {
				String sj = element.getSubj();
				if(element.getGroup_nums().contains(gr) && element.getSubj().contains(sj))
					return String.format("Lab%s: %s(%s) - max points:%s deadline:%s \n", element.getId(), element.getLab_name(), element.getSubj(), element.getMax_points(), element.getDeadline());
			}
		}
		return "\n";
	}

	@PostMapping(path="/login")
	public @ResponseBody String Sign_in(
			@RequestParam String login,
			@RequestParam String pass,
			HttpServletRequest request
	) {
		List<User> users = userRepository.findByLogin(login);

		User user = users.stream()
				.filter(usr -> usr.getHashedPass(pass).equals(usr.getPass()))
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with login=" + login));
		if (user.isSession_status()) {
			return ("This user is already logged in");
		} else {
			user.setSession_status(true);
			user.setIp_session(request.getRemoteAddr());
			userRepository.save(user);
			return String.format("Hello, %s %s %s", user.getFirstName(), user.getLastName(), request.getRemoteAddr());
		}
	}

	@PostMapping(path="/logout")
	public @ResponseBody String Quit_session(
			@RequestParam String login,
			@RequestParam String pass
	) {
		List<User> users = userRepository.findByLogin(login);

		User user = users.stream()
				.filter(usr -> usr.getHashedPass(pass).equals(usr.getPass()))
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with login=" + login));
		if (user.isSession_status()) {
			user.setSession_status(false);
			user.setIp_session("0");
			userRepository.save(user);
			return String.format("Bye, %s %s", user.getFirstName(), user.getLastName());
		} else {
			return ("The user is not logged in");
		}
	}
}