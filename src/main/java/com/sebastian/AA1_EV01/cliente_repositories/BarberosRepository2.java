package com.sebastian.AA1_EV01.cliente_repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sebastian.AA1_EV01.citas_models.Barberos;

public interface BarberosRepository2 extends JpaRepository<Barberos, Integer> {

    Barberos findByCedula(int cedula);
}
