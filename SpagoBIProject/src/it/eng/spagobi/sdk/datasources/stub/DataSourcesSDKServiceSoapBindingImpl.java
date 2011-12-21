/*
 * SpagoBI, the Open Source Business Intelligence suite
 * � 2005-2015 Engineering Group
 *
 * This file is part of SpagoBI. SpagoBI is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 2.1 of the License, or any later version. 
 * SpagoBI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. You should have received
 * a copy of the GNU Lesser General Public License along with SpagoBI. If not, see: http://www.gnu.org/licenses/.
 * The complete text of SpagoBI license is included in the COPYING.LESSER file. 
 */

package it.eng.spagobi.sdk.datasources.stub;

import it.eng.spagobi.sdk.datasets.impl.DataSetsSDKServiceImpl;
import it.eng.spagobi.sdk.datasources.impl.DataSourcesSDKServiceImpl;

public class DataSourcesSDKServiceSoapBindingImpl implements it.eng.spagobi.sdk.datasources.stub.DataSourcesSDKService{
    public it.eng.spagobi.sdk.datasources.bo.SDKDataSource getDataSource(java.lang.Integer in0) throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException {
    	DataSourcesSDKServiceImpl impl = new DataSourcesSDKServiceImpl();
    	return impl.getDataSource(in0);
    }

    public it.eng.spagobi.sdk.datasources.bo.SDKDataSource[] getDataSources() throws java.rmi.RemoteException, it.eng.spagobi.sdk.exceptions.NotAllowedOperationException {
    	DataSourcesSDKServiceImpl impl = new DataSourcesSDKServiceImpl();
    	return impl.getDataSources();
    }

}
