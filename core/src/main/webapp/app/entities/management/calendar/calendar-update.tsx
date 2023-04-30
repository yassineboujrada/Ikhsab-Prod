import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICalendar } from 'app/shared/model/management/calendar.model';
import { getEntity, updateEntity, createEntity, reset } from './calendar.reducer';

export const CalendarUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const calendarEntity = useAppSelector(state => state.core.calendar.entity);
  const loading = useAppSelector(state => state.core.calendar.loading);
  const updating = useAppSelector(state => state.core.calendar.updating);
  const updateSuccess = useAppSelector(state => state.core.calendar.updateSuccess);

  const handleClose = () => {
    navigate('/calendar' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.dateNaissance = convertDateTimeToServer(values.dateNaissance);
    values.velage = convertDateTimeToServer(values.velage);
    values.chaleur = convertDateTimeToServer(values.chaleur);
    values.insemination = convertDateTimeToServer(values.insemination);

    const entity = {
      ...calendarEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dateNaissance: displayDefaultDateTime(),
          velage: displayDefaultDateTime(),
          chaleur: displayDefaultDateTime(),
          insemination: displayDefaultDateTime(),
        }
      : {
          ...calendarEntity,
          dateNaissance: convertDateTimeFromServer(calendarEntity.dateNaissance),
          velage: convertDateTimeFromServer(calendarEntity.velage),
          chaleur: convertDateTimeFromServer(calendarEntity.chaleur),
          insemination: convertDateTimeFromServer(calendarEntity.insemination),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coreApp.managementCalendar.home.createOrEditLabel" data-cy="CalendarCreateUpdateHeading">
            <Translate contentKey="coreApp.managementCalendar.home.createOrEditLabel">Create or edit a Calendar</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="calendar-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('coreApp.managementCalendar.lactation')}
                id="calendar-lactation"
                name="lactation"
                data-cy="lactation"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.managementCalendar.jrsLact')}
                id="calendar-jrsLact"
                name="jrsLact"
                data-cy="jrsLact"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.managementCalendar.statutReproduction')}
                id="calendar-statutReproduction"
                name="statutReproduction"
                data-cy="statutReproduction"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.managementCalendar.etatProd')}
                id="calendar-etatProd"
                name="etatProd"
                data-cy="etatProd"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.managementCalendar.dateNaissance')}
                id="calendar-dateNaissance"
                name="dateNaissance"
                data-cy="dateNaissance"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('coreApp.managementCalendar.velage')}
                id="calendar-velage"
                name="velage"
                data-cy="velage"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('coreApp.managementCalendar.chaleur')}
                id="calendar-chaleur"
                name="chaleur"
                data-cy="chaleur"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('coreApp.managementCalendar.insemination')}
                id="calendar-insemination"
                name="insemination"
                data-cy="insemination"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('coreApp.managementCalendar.cowId')}
                id="calendar-cowId"
                name="cowId"
                data-cy="cowId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/calendar" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CalendarUpdate;
