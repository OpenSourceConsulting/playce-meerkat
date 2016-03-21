package com.athena.meerkat.controller.web.resources.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.athena.meerkat.controller.web.entities.NetworkInterface;

public interface NetworkInterfaceRepository extends
		JpaRepository<NetworkInterface, Integer> {

	NetworkInterface findByIpv4(String ipv4);

}
