package com.proj.web;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.proj.dao.FicheSuppVitaminesRepository;
import com.proj.dao.FicheVaccinRepository;
import com.proj.dao.StockRepository;
import com.proj.dao.UserRepository;
import com.proj.dao.VaccinRepository;
import com.proj.dao.CommandeRepository;
import com.proj.dao.EnfantRepository;

import com.proj.entities.Commande;
import com.proj.entities.FicheVaccin;
import com.proj.entities.User;
import com.proj.entities.Vaccin;



@Controller
public class StockController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private VaccinRepository vaccinRepository;
	
	@Autowired
	private CommandeRepository commandeRepository;
	
	@Autowired
	private FicheVaccinRepository ficheVaccinRepository;
	
	
	@RequestMapping(value="/operateur/stock")
	public String stock(Model model ,HttpServletRequest request) {
		
		Principal principal = request.getUserPrincipal();
        User user = userRepository.chercher(principal.getName());
        
        Long idcentre = user.getCentreSnate().getId();
		
        List <Vaccin> v = vaccinRepository.chercherstock(idcentre);
		model.addAttribute("listStock", v);
		
		return "stock";
	}
	
	
	@RequestMapping(value="/operateur/commande", method=RequestMethod.GET)
	public String formCommande(Model model, Long id) {
		
		Optional<Vaccin> v = vaccinRepository.findById(id);
		
		if(v.isPresent()) {
			Vaccin vaccin = v.get();
			model.addAttribute("vaccin", vaccin);
			model.addAttribute("commande", new Commande());
		}
		
		return "formCommande";
	}
	
	@RequestMapping(value="/operateur/saveCommande", method=RequestMethod.POST)
	public String saveCommande(Model model, @Valid Commande commande, BindingResult bindingResult,HttpServletRequest request, 
			Long id){
		if(bindingResult.hasErrors()) {
			return "formCommande";
		}
		
		
		Optional<Vaccin> v = vaccinRepository.findById(id);

		if(v.isPresent()) {
			Vaccin vaccin = v.get();
			commande.setVaccin(vaccin);
			commandeRepository.save(commande);
			  
			vaccinRepository.edit(vaccin.getQuantiteStock()+commande.getQuantite(), vaccin.getId());
		}

		

		return "redirect:/operateur/stock";
	}
	
	
	@RequestMapping(value="/gestionnaire/gestionStock")
	public String gestionDeStock(Model model, 
			@RequestParam(name = "dateDebut", defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateDebut,
			@RequestParam(name = "dateFin", defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFin,
			@RequestParam(name="typeVaccin", defaultValue="") String typeVaccin,
			HttpServletRequest request){
		
		Principal principal = request.getUserPrincipal();
        User user = userRepository.chercher(principal.getName());
        
        Long idcentre = user.getCentreSnate().getId();
		
		
		List<FicheVaccin> listeVaccinsPeriodeType = ficheVaccinRepository.vaccinsConosomesPeriodeType(dateDebut,dateFin,typeVaccin,idcentre); 
		int QuantiteCommandee= commandeRepository.commandeParPeriode(dateDebut, dateFin, typeVaccin, idcentre);
		
		int nombreVaccinsPeriodeType = listeVaccinsPeriodeType.size();
		
		model.addAttribute("nombreVaccinsPeriodeType", nombreVaccinsPeriodeType);
		model.addAttribute("QuantiteCommandee", QuantiteCommandee);
		
		return "gestionDeStock";
	}
	
}
