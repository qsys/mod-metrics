/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package be.qsys.vertx.metrics ;

import com.codahale.metrics.ObjectNameFactory ;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyValueObjectNameFactory implements ObjectNameFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(MetricsModule.class);
	private final String domainPrefix;
	
	KeyValueObjectNameFactory(String domain) {
		this.domainPrefix = domain + ":";
	}
	
	@Override
	public ObjectName  createName(String type, String domain, String keyvalues) {	
		try {
			ObjectName objectName = new ObjectName(domainPrefix + keyvalues);
			if (objectName.isPattern()) {
				objectName = new ObjectName(domain, "name", ObjectName.quote(keyvalues));
			}
			return objectName;
		} catch (MalformedObjectNameException e) {
			try {
				return new ObjectName(domain, "name", ObjectName.quote(keyvalues));
			} catch (MalformedObjectNameException e1) {
				LOGGER.warn("Unable to register {} {}", type, keyvalues, e1);
				throw new RuntimeException(e1);
			}
		}
	}
}
