package com.project.Mental.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.Mental.service.AppointmentService;

@Controller
public class CounselorAppointmentsController {

	private final AppointmentService appointmentService;

	public CounselorAppointmentsController(AppointmentService appointmentService) {
		this.appointmentService = appointmentService;
	}

	@GetMapping("/counselor/appointments")
	public String appointments(Model model) {
		model.addAttribute("pending", appointmentService.getPendingAppointments());
		model.addAttribute("confirmed", appointmentService.getConfirmedAppointments());
		return "counselor/appointments";
	}

	@PostMapping("/counselor/appointments/{id}/confirm")
	public String confirm(@PathVariable("id") Long id, RedirectAttributes ra) {
		try {
			appointmentService.confirm(id);
			ra.addFlashAttribute("success", "Appointment confirmed.");
		} catch (RuntimeException ex) {
			ra.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/counselor/appointments";
	}

	@PostMapping("/counselor/appointments/{id}/reject")
	public String reject(@PathVariable("id") Long id, RedirectAttributes ra) {
		try {
			appointmentService.reject(id);
			ra.addFlashAttribute("success", "Appointment rejected. Slot is now free.");
		} catch (RuntimeException ex) {
			ra.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/counselor/appointments";
	}
}
