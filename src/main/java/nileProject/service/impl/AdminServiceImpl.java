//package nileProject.service.impl;
//
//import nileProject.dto.AdminDto;
//import nileProject.model.Admin;
//import nileProject.repository.AdminRepository;
//import nileProject.repository.RoleRepository;
//import nileProject.service.AdminService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Arrays;
//
//@Service
//public class AdminServiceImpl implements AdminService {
//
//    @Autowired
//    private AdminRepository adminRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Override
//    public Admin findByUsername(String username) {
//
//        return adminRepository.findByUsername(username);
//    }
//
//    @Override
//    public Admin save(AdminDto adminDto) {
//        Admin admin = new Admin();
//        admin.setFirstName(adminDto.getFirstName());
//        admin.setLastName(adminDto.getLastName());
//        admin.setUserName(adminDto.getUserName());
//        admin.setPassword(adminDto.getPassword());
//        admin.setRoles(Arrays.asList(roleRepository.findByName("ADMIN")));
//
//        return adminRepository.save(admin);
//    }
//}
