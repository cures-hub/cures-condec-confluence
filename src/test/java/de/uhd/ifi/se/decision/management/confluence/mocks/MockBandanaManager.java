package de.uhd.ifi.se.decision.management.confluence.mocks;

import java.util.HashMap;
import java.util.Map;

import com.atlassian.bandana.BandanaContext;
import com.atlassian.bandana.BandanaManager;

public class MockBandanaManager implements BandanaManager {

	private Map<String, Object> objects = new HashMap<String, Object>();

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setValue(BandanaContext context, String key, Object value) {
		objects.put(key, value);
	}

	@Override
	public Object getValue(BandanaContext context, String key) {
		return objects.get(key);
	}

	@Override
	public Object getValue(BandanaContext context, String key, boolean lookUp) {
		return objects.get(key);
	}

	@Override
	public Iterable<String> getKeys(BandanaContext context) {
		return objects.keySet();
	}

	@Override
	public void removeValue(BandanaContext context, String key) {
		objects.remove(key);
	}

}
