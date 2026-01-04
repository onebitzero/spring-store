package com.yatc.helloworld.repositories;

import com.yatc.helloworld.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}
