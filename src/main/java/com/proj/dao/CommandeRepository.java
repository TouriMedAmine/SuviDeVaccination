package com.proj.dao;


import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proj.entities.Commande;


public interface CommandeRepository extends JpaRepository<Commande, Long>{
	
	@Query("select sum(c.quantite) from Commande c"
			+ " inner join Vaccin v on c.vaccin = v.id"
			+ " inner join Stock s on v.stock = s.id"
			+ " inner join CentreSante cs on s.centreSante = cs.id"
			+ " where c.date >= :dateDebut and c.date <= :dateFin"
			+ " and v.type_vaccin like :t"
			+ " and cs.id like :u")
	public int commandeParPeriode(@Param("dateDebut")Date dateDebut, @Param("dateFin")Date dateFin,@Param("t")String typeVaccin, @Param("u")Long idcentre);
}

