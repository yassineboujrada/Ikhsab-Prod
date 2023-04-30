import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Cow from './cow';
import CowDetail from './cow-detail';
import CowUpdate from './cow-update';
import CowDeleteDialog from './cow-delete-dialog';

const CowRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Cow />} />
    <Route path="new" element={<CowUpdate />} />
    <Route path=":id">
      <Route index element={<CowDetail />} />
      <Route path="edit" element={<CowUpdate />} />
      <Route path="delete" element={<CowDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CowRoutes;
