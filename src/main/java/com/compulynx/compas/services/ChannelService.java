package com.compulynx.compas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compulynx.compas.models.Channel;
import com.compulynx.compas.models.extras.ChannelRep;
import com.compulynx.compas.repositories.ChannelRepository;

@Service
public class ChannelService {
	
	@Autowired
	private ChannelRepository channelRepository;
	
	public List<ChannelRep> getChannels () {
		return channelRepository.getChannels();
	}

	public Channel upChannel(Channel channel) {
		// TODO Auto-generated method stub
		return channelRepository.save(channel);
	}

	public List<Channel> getChannelsToWaive() {
		// TODO Auto-generated method stub
		return channelRepository.getChannelsToWaive();
	}

	public int waiveChannel(int waivedBy, Long id) {
		// TODO Auto-generated method stub
		return channelRepository.waiveChannel(waivedBy,id);
	}

	public int approveChannelWaive(int waivedApprovedBy, Long id) {
		// TODO Auto-generated method stub
		return channelRepository.approveChannelWaive(waivedApprovedBy,id);
	}

	public List<Channel> getWaivedChannles() {
		// TODO Auto-generated method stub
		return channelRepository.getWaivedChannles();
	}

	public int recectChannel(int waivedApprovedBy, Long id) {
		// TODO Auto-generated method stub
		return channelRepository.rejectChannel(waivedApprovedBy,id);
	}
}
