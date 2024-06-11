package com.minkyu.moais.repository;

import com.minkyu.moais.entity.Todo;
import com.minkyu.moais.repositorycustom.TodoRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>, JpaSpecificationExecutor<Todo>, TodoRepositoryCustom {

}