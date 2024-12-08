package com.example.demo.repository;

import com.example.demo.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Integer> {
     List<HoaDonChiTiet> findByHoaDon_Id(Integer id);

     Optional<HoaDonChiTiet> deleteByHoaDon_Id(Integer integer);
}
