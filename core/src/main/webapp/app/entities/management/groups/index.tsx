import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Groups from './groups';
import GroupsDetail from './groups-detail';
import GroupsUpdate from './groups-update';
import GroupsDeleteDialog from './groups-delete-dialog';

const GroupsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Groups />} />
    <Route path="new" element={<GroupsUpdate />} />
    <Route path=":id">
      <Route index element={<GroupsDetail />} />
      <Route path="edit" element={<GroupsUpdate />} />
      <Route path="delete" element={<GroupsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GroupsRoutes;
