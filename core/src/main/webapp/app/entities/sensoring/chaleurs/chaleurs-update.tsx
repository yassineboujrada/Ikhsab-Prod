import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IChaleurs } from 'app/shared/model/sensoring/chaleurs.model';
import { getEntity, updateEntity, createEntity, reset } from './chaleurs.reducer';

export const ChaleursUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const chaleursEntity = useAppSelector(state => state.core.chaleurs.entity);
  const loading = useAppSelector(state => state.core.chaleurs.loading);
  const updating = useAppSelector(state => state.core.chaleurs.updating);
  const updateSuccess = useAppSelector(state => state.core.chaleurs.updateSuccess);

  const handleClose = () => {
    navigate('/chaleurs' + location.search);
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
      ...chaleursEntity,
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
          ...chaleursEntity,
          date: convertDateTimeFromServer(chaleursEntity.date),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coreApp.sensoringChaleurs.home.createOrEditLabel" data-cy="ChaleursCreateUpdateHeading">
            <Translate contentKey="coreApp.sensoringChaleurs.home.createOrEditLabel">Create or edit a Chaleurs</Translate>
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
                  id="chaleurs-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('coreApp.sensoringChaleurs.date')}
                id="chaleurs-date"
                name="date"
                data-cy="date"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('coreApp.sensoringChaleurs.jrsLact')}
                id="chaleurs-jrsLact"
                name="jrsLact"
                data-cy="jrsLact"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.sensoringChaleurs.temps')}
                id="chaleurs-temps"
                name="temps"
                data-cy="temps"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.sensoringChaleurs.groupeid')}
                id="chaleurs-groupeid"
                name="groupeid"
                data-cy="groupeid"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.sensoringChaleurs.enclosid')}
                id="chaleurs-enclosid"
                name="enclosid"
                data-cy="enclosid"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.sensoringChaleurs.activite')}
                id="chaleurs-activite"
                name="activite"
                data-cy="activite"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.sensoringChaleurs.facteurEleve')}
                id="chaleurs-facteurEleve"
                name="facteurEleve"
                data-cy="facteurEleve"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.sensoringChaleurs.suspect')}
                id="chaleurs-suspect"
                name="suspect"
                data-cy="suspect"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.sensoringChaleurs.actAugmentee')}
                id="chaleurs-actAugmentee"
                name="actAugmentee"
                data-cy="actAugmentee"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.sensoringChaleurs.alarmeChaleur')}
                id="chaleurs-alarmeChaleur"
                name="alarmeChaleur"
                data-cy="alarmeChaleur"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.sensoringChaleurs.pasDeChaleur')}
                id="chaleurs-pasDeChaleur"
                name="pasDeChaleur"
                data-cy="pasDeChaleur"
                type="text"
              />
              <ValidatedField
                label={translate('coreApp.sensoringChaleurs.cowId')}
                id="chaleurs-cowId"
                name="cowId"
                data-cy="cowId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/chaleurs" replace color="info">
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

export default ChaleursUpdate;
