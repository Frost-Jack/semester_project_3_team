package com.example.accessingdata;
import org.springframework.data.repository.CrudRepository;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface LabRepository extends CrudRepository<LabTerms, Integer> {

}