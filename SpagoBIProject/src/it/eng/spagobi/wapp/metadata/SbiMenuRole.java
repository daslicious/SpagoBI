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
package it.eng.spagobi.wapp.metadata;

import it.eng.spagobi.commons.metadata.SbiExtRoles;
import it.eng.spagobi.commons.metadata.SbiHibernateModel;



/**
 * SbiMenuRole generated by hbm2java
 */

public class SbiMenuRole  extends SbiHibernateModel {


    // Fields    

     private SbiMenuRoleId id;
     private SbiMenu sbiMenu;
     private SbiExtRoles sbiExtRoles;


    // Constructors

    /**
     * default constructor.
     */
    public SbiMenuRole() {
    }

    
    /**
     * full constructor.
     * 
     * @param id the id
     * @param sbiMenu the sbi menu
     * @param sbiExtRoles the sbi ext roles
     */
    public SbiMenuRole(SbiMenuRoleId id, SbiMenu sbiMenu, SbiExtRoles sbiExtRoles) {
        this.id = id;
        this.sbiMenu = sbiMenu;
        this.sbiExtRoles = sbiExtRoles;
    }
    

   
    // Property accessors

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public SbiMenuRoleId getId() {
        return this.id;
    }
    
    /**
     * Sets the id.
     * 
     * @param id the new id
     */
    public void setId(SbiMenuRoleId id) {
        this.id = id;
    }

    /**
     * Gets the sbi menu.
     * 
     * @return the sbi menu
     */
    public SbiMenu getSbiMenu() {
        return this.sbiMenu;
    }
    
    /**
     * Sets the sbi menu.
     * 
     * @param sbiMenu the new sbi menu
     */
    public void setSbiMenu(SbiMenu sbiMenu) {
        this.sbiMenu = sbiMenu;
    }

    /**
     * Gets the sbi ext roles.
     * 
     * @return the sbi ext roles
     */
    public SbiExtRoles getSbiExtRoles() {
        return this.sbiExtRoles;
    }
    
    /**
     * Sets the sbi ext roles.
     * 
     * @param sbiExtRoles the new sbi ext roles
     */
    public void setSbiExtRoles(SbiExtRoles sbiExtRoles) {
        this.sbiExtRoles = sbiExtRoles;
    }
   








}
