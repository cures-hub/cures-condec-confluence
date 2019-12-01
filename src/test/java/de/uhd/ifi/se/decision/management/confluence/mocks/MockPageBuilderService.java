package de.uhd.ifi.se.decision.management.confluence.mocks;

import java.util.Set;

import com.atlassian.webresource.api.assembler.AssembledResources;
import com.atlassian.webresource.api.assembler.PageBuilderService;
import com.atlassian.webresource.api.assembler.RequiredData;
import com.atlassian.webresource.api.assembler.RequiredResources;
import com.atlassian.webresource.api.assembler.WebResourceAssembler;

public class MockPageBuilderService implements PageBuilderService {

	@Override
	public WebResourceAssembler assembler() {
		return new WebResourceAssembler() {

			@Override
			public RequiredResources resources() {
				return new RequiredResources() {

					@Override
					public RequiredResources requireWebResource(String moduleCompleteKey) {
						return this;
					}

					@Override
					public RequiredResources requirePage(String key) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public RequiredResources requireModule(String name) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public RequiredResources requireContext(String context) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public RequiredResources exclude(Set<String> webResources, Set<String> contexts) {
						// TODO Auto-generated method stub
						return null;
					}
				};
			}

			@Override
			public RequiredData data() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public WebResourceAssembler copy() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public AssembledResources assembled() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	@Override
	public void seed(WebResourceAssembler webResourceAssembler) {
		// TODO Auto-generated method stub
	}
}