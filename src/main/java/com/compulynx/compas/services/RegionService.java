package com.compulynx.compas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.compulynx.compas.models.Branch;
import com.compulynx.compas.models.Country;
import com.compulynx.compas.repositories.BranchRepository;
import com.compulynx.compas.repositories.CountryRepository;

@Service
public class RegionService {
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private BranchRepository branchRepository;
	
	
	public List<Branch> getBranches() {
		return branchRepository.findAll();
	}

	public List<Country> getCountries() {
		return (List<Country>) countryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
	}

	public Country upCountry(Country country) {
		// TODO Auto-generated method stub
		return countryRepository.save(country);
	}

	public Branch upBranch(Branch branch) {
		// TODO Auto-generated method stub
		return branchRepository.save(branch);
	}
	
	public Country checkIfExists(String countryCode, String countryName) {
		return countryRepository.checkIfExists(countryCode, countryName);
	}
//	private Sort sortByIdAsc() {
//        return new Sort(Sort.Direction.ASC, "id");
//    }

	public Branch checkIfBranchExist(String branchCode, String branchName) {
		// TODO Auto-generated method stub
		return branchRepository.checkIfBranchExists(branchCode,branchName);
	}

	public List<Branch> getActiveBranches() {
		// TODO Auto-generated method stub
		return branchRepository.getActiveBranches();
	}

	public List<Country> getActiveCountries() {
		// TODO Auto-generated method stub
		return countryRepository.getActiveCountries();
	}

	public Branch getBranchToWaive(String branchCode) {
		// TODO Auto-generated method stub
		return branchRepository.getBranchToWaive(branchCode);
	}

	public List<Branch> getWaivedBranches() {
		// TODO Auto-generated method stub
		return branchRepository.getWaivedBranches();
	}

	public List<Country> getWaivedCountries() {
		// TODO Auto-generated method stub
		return countryRepository.getWaivedWaived();
	}

	public int waiveCountry(int waivedBy, Long id) {
		// TODO Auto-generated method stub
		return countryRepository.waiveCountry(waivedBy,id);
	}

	public List<Country> getCountriesToWaive() {
		// TODO Auto-generated method stub
		return countryRepository.getCountriesToWaive();
	}

	public int approveCountryWaive(int waivedApprovedBy, Long id) {
		// TODO Auto-generated method stub
		return countryRepository.approveCountryWaive(waivedApprovedBy,id);
	}

	public List<Branch> getBranchesToWaive() {
		// TODO Auto-generated method stub
		return branchRepository.getBranchesToWaive();
	}

	public int waiveBranch(int waivedBy, Long id) {
		// TODO Auto-generated method stub
		return branchRepository.waiveBranch(waivedBy, id);
	}

	public int approveBranchWaive(int waivedApprovedBy, Long id) {
		// TODO Auto-generated method stub
		return branchRepository.approveBranchWaive(waivedApprovedBy, id);
	}

	public int rejectBranchWaive(int waivedApprovedBy, Long id) {
		// TODO Auto-generated method stub
		return branchRepository.rejectBranchWaive(waivedApprovedBy, id);
	}

	public int rejectCountryWaive(int waivedApprovedBy, Long id) {
		// TODO Auto-generated method stub
		return countryRepository.rejectCountryWaive(waivedApprovedBy,id);
	}

	public List<Branch> getBranchesPrev(String status) {
		// TODO Auto-generated method stub
		return branchRepository.getBranchRpt(status);
	}

	public List<Branch> getActiveCountryBranches(String country) {
		// TODO Auto-generated method stub
		return branchRepository.getActiveCountryBranches(country);
	}
	
}
