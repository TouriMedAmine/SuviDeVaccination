package com.proj.dao;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.proj.entities.FicheVaccin;

public interface FicheVaccinRepository extends JpaRepository<FicheVaccin, Long>{
	
	@Modifying
	@Query("UPDATE FicheVaccin f SET f.etat = :x WHERE f.id = :y")
	
	@Transactional
	public void edit(@Param("x")boolean etat, @Param("y") Long id);
	
	
	@Query("select f from FicheVaccin f"
			+ " inner join CalendrierVaccination c on f.calendrierVaccination = c.id"
			+ " inner join Enfant e on c.enfant = e.id"
			+ " where f.date like :x and f.etat=0"
			+ " and e.centreSante.id like :u")
	public List<FicheVaccin> chercher(@Param("x")Date date, @Param("u")Long idcentre);
	
	
	@Query("select f from FicheVaccin f"
			+ "	inner join CalendrierVaccination c on f.calendrierVaccination = c.id "
			+ " inner join Enfant e on c.enfant = e.id "
			+ " where f.date like :x"
			+ " and f.type_vaccin like :y"
			+ " and e.sexe like :z"
			+ " and e.centreSante.id like :u")
	public List<FicheVaccin> recherchavancee(@Param("x")Date date, @Param("y")String type,@Param("z")String sexe, @Param("u")Long idcentre);
	
	@Query("select f from FicheVaccin f"
			+ "	inner join CalendrierVaccination c on f.calendrierVaccination = c.id "
			+ " inner join Enfant e on c.enfant = e.id "
			+ " where f.etat = 1"
			+ " and f.type_vaccin like :y"
			+ " and e.centreSante.id like :u")
	public List<FicheVaccin> recherchavanceeParTypeV(@Param("y")String type, @Param("u")Long idcentre);
	
	@Query("select f from FicheVaccin f"
			+ "	inner join CalendrierVaccination c on f.calendrierVaccination = c.id "
			+ " inner join Enfant e on c.enfant = e.id "
			+ " where f.etat = 1"
			+ " and e.sexe like :z"
			+ " and e.centreSante.id like :u")
	public List<FicheVaccin> recherchavanceeParSexe(@Param("z")String sexe, @Param("u")Long idcentre);
	
	@Query("select f from FicheVaccin f"
			+ " inner join CalendrierVaccination c on f.calendrierVaccination = c.id"
			+ " inner join Enfant e on c.enfant = e.id"
			+ " where f.date >= :debutMois and f.date <= :finMois and f.etat=0"
			+ " and e.centreSante.id like :u")
	public List<FicheVaccin> chercherParMois(@Param("debutMois")Date debutMois, @Param("finMois")Date finMois, @Param("u")Long idcentre);
	
	@Query("select f from FicheVaccin f"
			+ "	inner join CalendrierVaccination c on f.calendrierVaccination = c.id "
			+ " inner join Enfant e on c.enfant = e.id "
			+ " where f.date >= :dateDebut and f.date <= :dateFin and f.etat=1"
			+ " and f.type_vaccin like :y"
			+ " and e.centreSante.id like :u")
	public List<FicheVaccin> vaccinsConosomesPeriodeType(@Param("dateDebut")Date dateDebut, @Param("dateFin")Date dateFin, @Param("y")String type, @Param("u")Long idcentre);
	
}


