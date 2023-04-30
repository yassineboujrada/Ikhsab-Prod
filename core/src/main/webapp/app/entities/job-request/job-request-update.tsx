import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IJobRequest } from 'app/shared/model/job-request.model';
import { getEntity, updateEntity, createEntity, reset } from './job-request.reducer';

export const JobRequestUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const jobRequestEntity = useAppSelector(state => state.core.jobRequest.entity);
  const loading = useAppSelector(state => state.core.jobRequest.loading);
  const updating = useAppSelector(state => state.core.jobRequest.updating);
  const updateSuccess = useAppSelector(state => state.core.jobRequest.updateSuccess);

  const handleClose = () => {
    navigate('/job-request' + location.search);
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
    const entity = {
      ...jobRequestEntity,
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
      ? {}
      : {
          ...jobRequestEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coreApp.jobRequest.home.createOrEditLabel" data-cy="JobRequestCreateUpdateHeading">
            <Translate contentKey="coreApp.jobRequest.home.createOrEditLabel">Create or edit a JobRequest</Translate>
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
                  id="job-request-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('coreApp.jobRequest.consumer')}
                id="job-request-consumer"
                name="consumer"
                data-cy="consumer"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.jobRequest.provider')}
                id="job-request-provider"
                name="provider"
                data-cy="provider"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.jobRequest.serviceStatus')}
                id="job-request-serviceStatus"
                name="serviceStatus"
                data-cy="serviceStatus"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.jobRequest.roomId')}
                id="job-request-roomId"
                name="roomId"
                data-cy="roomId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/job-request" replace color="info">
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

export default JobRequestUpdate;
