package com.example.accessingdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.util.Arrays;
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

	@PostMapping(path="/all")
	public @ResponseBody
	StringBuffer getAllLabs(@RequestParam String subj, HttpServletRequest request) {
		User user = userRepository.findByIp(request.getRemoteAddr()).stream().findFirst().orElseThrow(()
				-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unregistered user"));
		String group = user.getStat();
		Iterable<LabTerms> all_labs = labRepository.findAll();
		StringBuffer str = new StringBuffer();
		if (group.equals("-1")){
			String leftAlignFormat = "| %-4d | %-20s | %-20d | %-10d | %-39s |\n";
			str.append("+------+----------------------+----------------------+------------+-----------------------------------------+\n");
			str.append("+  ID  |     Name of lab      |       Deadline       | Max points | Available groups                        |\n");
			str.append("+------+----------------------+----------------------+------------+-----------------------------------------+\n");
			for (LabTerms term : all_labs) {
				if (!term.getSubj().equals(subj)){
					continue;
				}else{
					String[] pupils = term.getGroup_nums().split("/");
					str.append(String.format(leftAlignFormat, term.getId(), term.getLab_name(), term.getDeadline(), term.getMax_points(), Arrays.toString(Arrays.copyOfRange(pupils, 1, pupils.length))));
				}
				str.append("+------+----------------------+----------------------+------------+-----------------------------------------+\n");}
		}else{
			String leftAlignFormat = "| %-4d | %-20s | %-20d | %-10d |\n";
		str.append("+------+----------------------+----------------------+------------+\n");
		str.append("|  ID  |     Name of lab      |       Deadline       | Max points |\n");
		str.append("+------+----------------------+----------------------+------------+\n");
		for (LabTerms term : all_labs) {
			if (!term.getSubj().equals(subj)){
				continue;
			}else{
				if ((term.getGroup_nums().contains(group))) {
					str.append(String.format(leftAlignFormat, term.getId(), term.getLab_name(), term.getDeadline(), term.getMax_points()));
				}
			}
			str.append("+------+----------------------+----------------------+------------+\n");
		}}
		return str;
	}

//	@PostMapping(path="/upload")
//	public @ResponseBody String upload_file (
//			@RequestParam MultipartFile file
//	) {
//		String uploadDir = "С:/upload/";
//		File transferFile = new File(file.getOriginalFilename());
//		return "Saved";
//	}


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
			user.setIp(request.getRemoteAddr());
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
			user.setIp("0");
			userRepository.save(user);
			return String.format("Bye, %s %s", user.getFirstName(), user.getLastName());
		} else {
			return ("The user is not logged in");
		}
	}
}