package com.kbstar.itsm.test;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

public class SampleBaseTestCase {
	
	
	@BeforeEach
	public void onceSetup() {
		MockitoAnnotations.initMocks(this);
	}

}
