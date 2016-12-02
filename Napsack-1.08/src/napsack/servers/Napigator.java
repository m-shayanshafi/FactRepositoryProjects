// Written by Chris Redekop <waveform@users.sourceforge.net>
// Released under the conditions of the GPL (See below).
//
// Thanks to Travis Whitton for the Gnap Fetch code used by Napsack.
//
// Also thanks to Radovan Garabik for the pynap code used by Napsack.
//
// Napsack - a specialized client for launching cross-server Napster queries.
// Copyright (C) 2000-2002 Chris Redekop
// 
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2
// of the License, or (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA

package napsack.servers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import napsack.event.NapigatorEvent;
import napsack.event.NapigatorListener;
import napsack.util.properties.ServersFileProperty;

public abstract class Napigator {
	public final static String NA = "n/a";

	private final static long REFRESH_PERIOD = 3600000;
	private final static String DELIMITER = " ";
	private final static String NOT_APPLICABLE = "-1";
	private final static int NUM_PROPERTIES = 14;
	private final static int IP = 1;
	private final static int PORT = 2;
	private final static int NETWORK_NAME = 3;
	private final static int USERS = 5;
	private final static int FILES = 6;
	private final static int GIGS = 7;
	private final static int NAME = 8;
	private final static NapsterService[] NAPSTER_SERVICES = new NapsterService[0];
	private final static String[] STRINGS = new String[0];

	private static Napigator instance;

	private static boolean failsMinCriteria(final String[] napsterServiceProperties_, final ServiceStatistics minStatistics_) {
		boolean fails_ = false;

		final Integer minFiles_ = minStatistics_.getFiles();

		if (!napsterServiceProperties_[FILES].equals(NOT_APPLICABLE) && minFiles_ != null && Integer.parseInt(napsterServiceProperties_[FILES]) < minFiles_.intValue()) {
			fails_ = true;
		} else {
			final Integer minGigs_ = minStatistics_.getGigs();

			if (!napsterServiceProperties_[GIGS].equals(NOT_APPLICABLE) && minGigs_ != null && Integer.parseInt(napsterServiceProperties_[GIGS]) < minGigs_.intValue()) {
				fails_ = true;
			} else {
				final int serviceUsers_ = Integer.parseInt(napsterServiceProperties_[USERS]);
				final Integer minUsers_ = minStatistics_.getUsers();

				if (serviceUsers_ > -1 && minUsers_ != null && serviceUsers_ < minUsers_.intValue()) {
					fails_ = true;
				}
			}
		}

		return fails_;
	}

	public static Napigator getInstance() {
		if (instance == null) {
			synchronized (Napigator.class) {
				if (instance == null) {
					final String serversFileName_ = (String) ServersFileProperty.getInstance().getValue();

					if (serversFileName_ != null) {
						instance = new FileNapigator(serversFileName_);
					} else {
						instance = new NetNapigator();
					}
				}
			}
		}
		
		return instance;
	}

	private static void populateNapsterProperties(final String[] napsterServiceProperties_, final String propertyString_) {
		final StringTokenizer napsterServiceTokenizer_ = new StringTokenizer(propertyString_, DELIMITER);

		for (int i = 0; i < NUM_PROPERTIES; ++i) {
			napsterServiceProperties_[i] = napsterServiceTokenizer_.nextToken();
		}
	}

	private List internalNapsterServices;
	private Date lastRefreshed;
	private volatile long refreshPeriod;
	private volatile boolean refreshing;
	private final List napigatorListeners;

	protected Napigator() {
		refreshPeriod = REFRESH_PERIOD;
		napigatorListeners = new ArrayList();
	}

	public void addNapigatorListener(final NapigatorListener napigatorListener_) {
		final List napigatorListeners_ = getNapigatorListeners();

		synchronized (napigatorListeners_) {
			napigatorListeners_.add(napigatorListener_);
		}
	}

	private void conditionallyRefresh() throws IOException {
		Date lastRefreshed_;
		long refreshPeriod_;
		long currentTimeMillis_;

		synchronized (this) {
			lastRefreshed_ = getLastRefreshed();
			refreshPeriod_ = getRefreshPeriod();
			currentTimeMillis_ = System.currentTimeMillis();
		}

		if (lastRefreshed_ == null || currentTimeMillis_ - lastRefreshed_.getTime() >= refreshPeriod_) {
			refreshNapsterServices();
		}
	}

	protected abstract BufferedReader createBufferedReader() throws IOException;

	private void fireRefreshedEvent() {
		final NapigatorEvent napigatorEvent_ = new NapigatorEvent(this);
		final List napigatorListeners_ = getNapigatorListeners();

		synchronized (napigatorListeners_) {
			final int size_ = napigatorListeners_.size();

			for (int i = 0; i < size_; ++i) {
				((NapigatorListener) napigatorListeners_.get(i)).napigatorRefreshed(napigatorEvent_);
			}
		}
	}
		
	private void fireRefreshingEvent() {
		final NapigatorEvent napigatorEvent_ = new NapigatorEvent(this);
		final List napigatorListeners_ = getNapigatorListeners();

		synchronized (napigatorListeners_) {
			final int size_ = napigatorListeners_.size();

			for (int i = 0; i < size_; ++i) {
				((NapigatorListener) napigatorListeners_.get(i)).napigatorRefreshing(napigatorEvent_);
			}
		}
	}
		
	private List getInternalNapsterServices() {
		if (internalNapsterServices == null) {
			internalNapsterServices = new ArrayList();
		}

		return internalNapsterServices;
	}

	private synchronized Date getLastRefreshed() {
		return lastRefreshed;
	}

	private List getNapigatorListeners() {
		return napigatorListeners;
	}

	public NapsterService[] getNapsterServices(final String[] excludedNetworks_, final ServiceStatistics minStatistics_) throws IOException {
		conditionallyRefresh();

		final String[] napsterServiceProperties_ = new String[NUM_PROPERTIES];
		final HashMap napsterServices_ = new HashMap();

		NapsterServer napsterServer_ = null;
		Integer files_ = null;
		Integer gigs_ = null;
		Integer users_ = null;
		int naCount = 0;

		Arrays.sort(excludedNetworks_);

		synchronized (this) {
			final List internalNapsterServices_ = getInternalNapsterServices();
			final int size_ = internalNapsterServices_.size();

			for (int i = 0; i < size_; ++i) { 
				try {
					populateNapsterProperties(napsterServiceProperties_, (String) internalNapsterServices_.get(i));	
				} catch(NoSuchElementException noSuchElementException_) {
					continue;
				}

				if (Arrays.binarySearch(excludedNetworks_, napsterServiceProperties_[NETWORK_NAME]) >= 0 || failsMinCriteria(napsterServiceProperties_, minStatistics_)) {
					continue;
				}

				files_ = napsterServiceProperties_[FILES].equals(NOT_APPLICABLE) ? null : new Integer(napsterServiceProperties_[FILES]);  
				gigs_ = napsterServiceProperties_[GIGS].equals(NOT_APPLICABLE) ? null : new Integer(napsterServiceProperties_[GIGS]);
				users_ = napsterServiceProperties_[USERS].equals(NOT_APPLICABLE) ? null: new Integer(napsterServiceProperties_[USERS]);

				napsterServer_ = new NapsterServer(new ServiceAddress(napsterServiceProperties_[IP], new Integer(napsterServiceProperties_[PORT]), napsterServiceProperties_[NAME]), new ServiceStatistics(files_, gigs_, users_));

				if (napsterServiceProperties_[NETWORK_NAME].equals(NA)) {
					napsterServer_.setParentNetworkName(NA);
					napsterServices_.put(new Integer(naCount++), napsterServer_);
				} else {
					NapsterNetwork napsterNetwork_ = (NapsterNetwork) napsterServices_.get(napsterServiceProperties_[NETWORK_NAME]);

					if (napsterNetwork_ == null) {
						napsterNetwork_ = new NapsterNetwork(napsterServiceProperties_[NETWORK_NAME]);
						napsterServices_.put(napsterServiceProperties_[NETWORK_NAME], napsterNetwork_);
					}

					napsterNetwork_.addNapsterService(napsterServer_);
				}
			}
		}

		return (NapsterService[]) napsterServices_.values().toArray(NAPSTER_SERVICES);
	}

	public String[] getNetworkNames() throws IOException {
		conditionallyRefresh();

		final ArrayList networkNames_ = new ArrayList();
		final String[] napsterServiceProperties_ = new String[NUM_PROPERTIES];

		synchronized (this) {
			final List internalNapsterServices_ = getInternalNapsterServices();
			final int size_ = internalNapsterServices_.size();

			for (int i = 0; i < size_; ++i) {
				populateNapsterProperties(napsterServiceProperties_, (String) internalNapsterServices_.get(i));

				if (!networkNames_.contains(napsterServiceProperties_[NETWORK_NAME])) {
					networkNames_.add(napsterServiceProperties_[NETWORK_NAME]);
				}
			}

		}

		return (String[]) networkNames_.toArray(STRINGS);
	}

	public long getRefreshPeriod() {
		return refreshPeriod;
	}

	public boolean isRefreshing() {
		return refreshing;
	}

	public void refreshNapsterServices() throws IOException {
		setRefreshing(true);
		fireRefreshingEvent();

		try {
			synchronized(this) {
				final BufferedReader bufferedReader_ = createBufferedReader();
				final List internalNapsterServices_ = getInternalNapsterServices();
				String internalNapsterService_ = null;

				internalNapsterServices_.clear();

				while ((internalNapsterService_ = bufferedReader_.readLine()) != null) {
					internalNapsterServices_.add(internalNapsterService_);	
				}

				internalNapsterServices_.remove(internalNapsterServices_.size() - 1);
				setLastRefreshed(new Date());
				bufferedReader_.close();
			}
		} finally {
			setRefreshing(false);
			fireRefreshedEvent();
		}
	}

	public void removeNapigatorListener(final NapigatorListener napigatorListener_) {
		final List napigatorListeners_ = getNapigatorListeners();

		synchronized (napigatorListeners_) {
			napigatorListeners_.remove(napigatorListener_);
		}
	}

	private synchronized void setLastRefreshed(final Date lastRefreshed_) {
		lastRefreshed = lastRefreshed_;
	}

	private void setRefreshPeriod(final long refreshPeriod_) {
		refreshPeriod = refreshPeriod_;
	}

	private void setRefreshing(final boolean refreshing_) {
		refreshing = refreshing_;
	}
}

