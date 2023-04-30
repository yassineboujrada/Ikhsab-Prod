import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import JobRequest from './job-request';
import JobRequestDetail from './job-request-detail';
import JobRequestUpdate from './job-request-update';
import JobRequestDeleteDialog from './job-request-delete-dialog';

const JobRequestRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<JobRequest />} />
    <Route path="new" element={<JobRequestUpdate />} />
    <Route path=":id">
      <Route index element={<JobRequestDetail />} />
      <Route path="edit" element={<JobRequestUpdate />} />
      <Route path="delete" element={<JobRequestDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default JobRequestRoutes;
