import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISante } from 'app/shared/model/sensoring/sante.model';
import { getEntity, updateEntity, createEntity, reset } from './sante.reducer';

export const SanteUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const santeEntity = useAppSelector(state => state.core.sante.entity);
  const loading = useAppSelector(state => state.core.sante.loading);
  const updating = useAppSelector(state => state.core.sante.updating);
  const updateSuccess = useAppSelector(state => state.core.sante.updateSuccess);

  const handleClose = () => {
    navigate('/sante' + location.search);
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
    values.date = convertDateTimeToServer(values.date);

    const entity = {
      ...santeEntity,
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
          date: displayDefaultDateTime(),
        }
      : {
          ...santeEntity,
          date: convertDateTimeFromServer(santeEntity.date),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coreApp.sensoringSante.home.createOrEditLabel" data-cy="SanteCreateUpdateHeading">
            <Translate contentKey="coreApp.sensoringSante.home.createOrEditLabel">Create or edit a Sante</Translate>
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
                  id="sante-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('coreApp.sensoringSante.date')}
                id="sante-date"
                name="date"
                data-cy="date"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('coreApp.sensoringSante.dureePositionCouchee')}
                id="sante-dureePositionCouchee"
                name="dureePositionCouchee"
                data-cy="dureePositionCouchee"
                type="text"
              />
              <ValidatedField label={translate('coreApp.sensoringSante.leve')} id="sante-leve" name="leve" data-cy="leve" type="text" />
              <ValidatedField label={translate('coreApp.sensoringSante.pas')} id="sante-pas" name="pas" data-cy="pas" type="text" />
              <ValidatedField label={translate('coreApp.sensoringSante.cowId')} id="sante-cowId" name="cowId" data-cy="cowId" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/sante" replace color="info">
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

export default SanteUpdate;
