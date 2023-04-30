import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Chaleurs from './chaleurs';
import ChaleursDetail from './chaleurs-detail';
import ChaleursUpdate from './chaleurs-update';
import ChaleursDeleteDialog from './chaleurs-delete-dialog';

const ChaleursRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Chaleurs />} />
    <Route path="new" element={<ChaleursUpdate />} />
    <Route path=":id">
      <Route index element={<ChaleursDetail />} />
      <Route path="edit" element={<ChaleursUpdate />} />
      <Route path="delete" element={<ChaleursDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ChaleursRoutes;
