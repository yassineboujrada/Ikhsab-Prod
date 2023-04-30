import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './groups.reducer';

export const GroupsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const groupsEntity = useAppSelector(state => state.core.groups.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="groupsDetailsHeading">
          <Translate contentKey="coreApp.managementGroups.detail.title">Groups</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="coreApp.managementGroups.id">Id</Translate>
            </span>
          </dt>
          <dd>{groupsEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="coreApp.managementGroups.name">Name</Translate>
            </span>
          </dt>
          <dd>{groupsEntity.name}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="coreApp.managementGroups.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{groupsEntity.userId}</dd>
          <dt>
            <span id="gpsAdress">
              <Translate contentKey="coreApp.managementGroups.gpsAdress">Gps Adress</Translate>
            </span>
          </dt>
          <dd>{groupsEntity.gpsAdress}</dd>
        </dl>
        <Button tag={Link} to="/groups" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/groups/${groupsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GroupsDetail;
