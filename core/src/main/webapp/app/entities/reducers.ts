import calendar from 'app/entities/management/calendar/calendar.reducer';
import chaleurs from 'app/entities/sensoring/chaleurs/chaleurs.reducer';
import cow from 'app/entities/management/cow/cow.reducer';
import enclos from 'app/entities/management/enclos/enclos.reducer';
import groups from 'app/entities/management/groups/groups.reducer';
import sante from 'app/entities/sensoring/sante/sante.reducer';
import profile from 'app/entities/management/profile/profile.reducer';
import jobRequest from 'app/entities/notification/job-request/job-request.reducer';
import message from 'app/entities/notification/message/message.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  calendar,
  chaleurs,
  cow,
  enclos,
  groups,
  sante,
  profile,
  jobRequest,
  message,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
