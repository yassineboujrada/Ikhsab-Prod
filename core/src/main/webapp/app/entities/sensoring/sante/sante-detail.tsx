import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './sante.reducer';

export const SanteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const santeEntity = useAppSelector(state => state.core.sante.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="santeDetailsHeading">
          <Translate contentKey="coreApp.sensoringSante.detail.title">Sante</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{santeEntity.id}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="coreApp.sensoringSante.date">Date</Translate>
            </span>
          </dt>
          <dd>{santeEntity.date ? <TextFormat value={santeEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="dureePositionCouchee">
              <Translate contentKey="coreApp.sensoringSante.dureePositionCouchee">Duree Position Couchee</Translate>
            </span>
          </dt>
          <dd>{santeEntity.dureePositionCouchee}</dd>
          <dt>
            <span id="leve">
              <Translate contentKey="coreApp.sensoringSante.leve">Leve</Translate>
            </span>
          </dt>
          <dd>{santeEntity.leve}</dd>
          <dt>
            <span id="pas">
              <Translate contentKey="coreApp.sensoringSante.pas">Pas</Translate>
            </span>
          </dt>
          <dd>{santeEntity.pas}</dd>
          <dt>
            <span id="cowId">
              <Translate contentKey="coreApp.sensoringSante.cowId">Cow Id</Translate>
            </span>
          </dt>
          <dd>{santeEntity.cowId}</dd>
        </dl>
        <Button tag={Link} to="/sante" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sante/${santeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SanteDetail;
