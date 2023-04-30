import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/calendar">
        <Translate contentKey="global.menu.entities.managementCalendar" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/chaleurs">
        <Translate contentKey="global.menu.entities.sensoringChaleurs" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cow">
        <Translate contentKey="global.menu.entities.managementCow" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/enclos">
        <Translate contentKey="global.menu.entities.managementEnclos" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/groups">
        <Translate contentKey="global.menu.entities.managementGroups" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sante">
        <Translate contentKey="global.menu.entities.sensoringSante" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/profile">
        <Translate contentKey="global.menu.entities.managementProfile" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/job-request">
        <Translate contentKey="global.menu.entities.notificationJobRequest" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/message">
        <Translate contentKey="global.menu.entities.notificationMessage" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
