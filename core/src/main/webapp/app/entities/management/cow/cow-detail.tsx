import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cow.reducer';

export const CowDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cowEntity = useAppSelector(state => state.core.cow.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cowDetailsHeading">
          <Translate contentKey="coreApp.managementCow.detail.title">Cow</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cowEntity.id}</dd>
          <dt>
            <span id="numero">
              <Translate contentKey="coreApp.managementCow.numero">Numero</Translate>
            </span>
          </dt>
          <dd>{cowEntity.numero}</dd>
          <dt>
            <span id="groupeId">
              <Translate contentKey="coreApp.managementCow.groupeId">Groupe Id</Translate>
            </span>
          </dt>
          <dd>{cowEntity.groupeId}</dd>
          <dt>
            <span id="enclosId">
              <Translate contentKey="coreApp.managementCow.enclosId">Enclos Id</Translate>
            </span>
          </dt>
          <dd>{cowEntity.enclosId}</dd>
          <dt>
            <span id="repondeur">
              <Translate contentKey="coreApp.managementCow.repondeur">Repondeur</Translate>
            </span>
          </dt>
          <dd>{cowEntity.repondeur}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="coreApp.managementCow.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{cowEntity.nom}</dd>
          <dt>
            <span id="deviceId">
              <Translate contentKey="coreApp.managementCow.deviceId">Device Id</Translate>
            </span>
          </dt>
          <dd>{cowEntity.deviceId}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="coreApp.managementCow.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{cowEntity.userId}</dd>
        </dl>
        <Button tag={Link} to="/cow" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cow/${cowEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CowDetail;
