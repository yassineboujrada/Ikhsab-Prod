import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Sante from './sante';
import SanteDetail from './sante-detail';
import SanteUpdate from './sante-update';
import SanteDeleteDialog from './sante-delete-dialog';

const SanteRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Sante />} />
    <Route path="new" element={<SanteUpdate />} />
    <Route path=":id">
      <Route index element={<SanteDetail />} />
      <Route path="edit" element={<SanteUpdate />} />
      <Route path="delete" element={<SanteDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SanteRoutes;
