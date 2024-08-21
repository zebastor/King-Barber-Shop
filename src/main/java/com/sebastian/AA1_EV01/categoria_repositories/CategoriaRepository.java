package com.sebastian.AA1_EV01.categoria_repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sebastian.AA1_EV01.citas_models.Categorias;

public interface CategoriaRepository extends JpaRepository<Categorias, Integer> {

}
