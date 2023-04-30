import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import { ReducersMapObject, combineReducers } from '@reduxjs/toolkit';

import getStore from 'app/config/store';

import entitiesReducers from './reducers';

import Calendar from './management/calendar';
import Chaleurs from './sensoring/chaleurs';
import Cow from './management/cow';
import Enclos from './management/enclos';
import Groups from './management/groups';
import Sante from './sensoring/sante';
import Profile from './management/profile';
import JobRequest from './notification/job-request';
import Message from './notification/message';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  const store = getStore();
  store.injectReducer('core', combineReducers(entitiesReducers as ReducersMapObject));
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="calendar/*" element={<Calendar />} />
        <Route path="chaleurs/*" element={<Chaleurs />} />
        <Route path="cow/*" element={<Cow />} />
        <Route path="enclos/*" element={<Enclos />} />
        <Route path="groups/*" element={<Groups />} />
        <Route path="sante/*" element={<Sante />} />
        <Route path="profile/*" element={<Profile />} />
        <Route path="job-request/*" element={<JobRequest />} />
        <Route path="message/*" element={<Message />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
