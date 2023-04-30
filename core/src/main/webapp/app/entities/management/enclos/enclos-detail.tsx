import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './enclos.reducer';

export const EnclosDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const enclosEntity = useAppSelector(state => state.core.enclos.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="enclosDetailsHeading">
          <Translate contentKey="coreApp.managementEnclos.detail.title">Enclos</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="coreApp.managementEnclos.id">Id</Translate>
            </span>
          </dt>
          <dd>{enclosEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="coreApp.managementEnclos.name">Name</Translate>
            </span>
          </dt>
          <dd>{enclosEntity.name}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="coreApp.managementEnclos.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{enclosEntity.userId}</dd>
          <dt>
            <span id="groupId">
              <Translate contentKey="coreApp.managementEnclos.groupId">Group Id</Translate>
            </span>
          </dt>
          <dd>{enclosEntity.groupId}</dd>
        </dl>
        <Button tag={Link} to="/enclos" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/enclos/${enclosEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EnclosDetail;
