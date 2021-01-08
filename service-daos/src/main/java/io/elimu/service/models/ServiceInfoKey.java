// Copyright 2018-2020 Elimu Informatics
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package io.elimu.service.models;

import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class ServiceInfoKey implements Serializable {

	private static final long serialVersionUID = 1L;

	String id;
	Long version;

	public ServiceInfoKey() {
	}

	public ServiceInfoKey(String id, Long version) {
		super();
		this.id = id;
		this.version = version;
	}

	public String getId() {
		return id;
	}

	public Long getVersion() {
		return version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceInfoKey other = (ServiceInfoKey) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version)) {
			return false;
		}
		return true;
	}

	@Override
    public String toString() {
        return "ServiceInfoKey {" +
                "id='" + id + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

}
