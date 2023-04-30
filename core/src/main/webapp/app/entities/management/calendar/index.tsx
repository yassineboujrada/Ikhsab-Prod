import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Calendar from './calendar';
import CalendarDetail from './calendar-detail';
import CalendarUpdate from './calendar-update';
import CalendarDeleteDialog from './calendar-delete-dialog';

const CalendarRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Calendar />} />
    <Route path="new" element={<CalendarUpdate />} />
    <Route path=":id">
      <Route index element={<CalendarDetail />} />
      <Route path="edit" element={<CalendarUpdate />} />
      <Route path="delete" element={<CalendarDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CalendarRoutes;
