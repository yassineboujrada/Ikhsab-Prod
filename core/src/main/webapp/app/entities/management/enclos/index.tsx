import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Enclos from './enclos';
import EnclosDetail from './enclos-detail';
import EnclosUpdate from './enclos-update';
import EnclosDeleteDialog from './enclos-delete-dialog';

const EnclosRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Enclos />} />
    <Route path="new" element={<EnclosUpdate />} />
    <Route path=":id">
      <Route index element={<EnclosDetail />} />
      <Route path="edit" element={<EnclosUpdate />} />
      <Route path="delete" element={<EnclosDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EnclosRoutes;
