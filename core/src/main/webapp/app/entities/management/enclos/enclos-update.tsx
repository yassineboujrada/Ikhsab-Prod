import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEnclos } from 'app/shared/model/management/enclos.model';
import { getEntity, updateEntity, createEntity, reset } from './enclos.reducer';

export const EnclosUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const enclosEntity = useAppSelector(state => state.core.enclos.entity);
  const loading = useAppSelector(state => state.core.enclos.loading);
  const updating = useAppSelector(state => state.core.enclos.updating);
  const updateSuccess = useAppSelector(state => state.core.enclos.updateSuccess);

  const handleClose = () => {
    navigate('/enclos' + location.search);
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
      ...enclosEntity,
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
          ...enclosEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coreApp.managementEnclos.home.createOrEditLabel" data-cy="EnclosCreateUpdateHeading">
            <Translate contentKey="coreApp.managementEnclos.home.createOrEditLabel">Create or edit a Enclos</Translate>
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
                  id="enclos-id"
                  label={translate('coreApp.managementEnclos.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('coreApp.managementEnclos.name')} id="enclos-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                label={translate('coreApp.managementEnclos.userId')}
                id="enclos-userId"
                name="userId"
                data-cy="userId"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.managementEnclos.groupId')}
                id="enclos-groupId"
                name="groupId"
                data-cy="groupId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/enclos" replace color="info">
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

export default EnclosUpdate;
