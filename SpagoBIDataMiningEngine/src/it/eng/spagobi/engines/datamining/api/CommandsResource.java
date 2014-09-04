/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.engines.datamining.api;

import it.eng.spagobi.engines.datamining.DataMiningEngineInstance;
import it.eng.spagobi.engines.datamining.common.AbstractDataMiningEngineService;
import it.eng.spagobi.engines.datamining.common.utils.DataMiningConstants;
import it.eng.spagobi.engines.datamining.model.DataMiningCommand;

import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;

@Path("/1.0/command")
public class CommandsResource extends AbstractDataMiningEngineService {
	public static transient Logger logger = Logger.getLogger(CommandsResource.class);

	@GET
	@Produces("text/html; charset=UTF-8")
	public String getCommands() {
		logger.debug("IN");

		DataMiningEngineInstance dataMiningEngineInstance = getDataMiningEngineInstance();
		String commandsJson = "";
		List<DataMiningCommand> commands = null;
		if (dataMiningEngineInstance.getCommands() != null && !dataMiningEngineInstance.getCommands().isEmpty()) {
			commands = dataMiningEngineInstance.getCommands();
			commandsJson = serializeList(commands);
		}

		if (!isNullOrEmpty(commandsJson)) {
			logger.debug("Returning commands list");
		} else {
			logger.debug("No commands list found");
		}

		logger.debug("OUT");
		return commandsJson;
	}

	@GET
	@Path("/{command}")
	@Produces("text/html; charset=UTF-8")
	public String setAutoMode(@PathParam("command") String commandName) {
		logger.debug("IN");

		DataMiningEngineInstance dataMiningEngineInstance = getDataMiningEngineInstance();
		List<DataMiningCommand> commands = null;
		if (dataMiningEngineInstance.getCommands() != null && !dataMiningEngineInstance.getCommands().isEmpty()) {
			commands = dataMiningEngineInstance.getCommands();
			for (Iterator it = commands.iterator(); it.hasNext();) {
				DataMiningCommand cmd = (DataMiningCommand) it.next();
				if (cmd.getName().equals(commandName)) {
					cmd.setMode(DataMiningConstants.EXECUTION_TYPE_AUTO);
				} else {
					cmd.setMode(DataMiningConstants.EXECUTION_TYPE_MANUAL);
				}
			}
		}
		logger.debug("OUT");
		return getJsonSuccess();
	}
}
