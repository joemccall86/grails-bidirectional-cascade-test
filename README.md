# grails-bidirectional-cascade-test
A test grails application to explore the capabilities of cascading saves in a bidirectional domain model

## The Issue
We have a domain class called Team

```
Team ('San Diego Padres')
-> Player ('John Doe')
  -> Contract (4 years, '$40,000,000')

```

When I save the team object with new players, the expected behavior is for team to save any unsaved players (since Player
belongsTo Team), and for that save of Player to trigger another save of Contract (since Contract belongsTo Player).

This behavior works as expected in Grails 2.4.5, but breaks in Grails 3.0.8. It seems like a bug since the thrown error is a 
`NullPointerException`:

```
java.lang.NullPointerException
	at org.hibernate.engine.spi.BatchFetchQueue.removeBatchLoadableEntityKey(BatchFetchQueue.java:163)
	at org.hibernate.engine.internal.StatefulPersistenceContext.addEntity(StatefulPersistenceContext.java:389)
	at org.hibernate.engine.internal.StatefulPersistenceContext.addEntity(StatefulPersistenceContext.java:462)
	at org.hibernate.action.internal.AbstractEntityInsertAction.makeEntityManaged(AbstractEntityInsertAction.java:143)
	at org.hibernate.engine.spi.ActionQueue.addResolvedEntityInsertAction(ActionQueue.java:203)
	at org.hibernate.engine.spi.ActionQueue.addInsertAction(ActionQueue.java:181)
	at org.hibernate.engine.spi.ActionQueue.addAction(ActionQueue.java:216)
	at org.hibernate.event.internal.AbstractSaveEventListener.addInsertAction(AbstractSaveEventListener.java:334)
	at org.hibernate.event.internal.AbstractSaveEventListener.performSaveOrReplicate(AbstractSaveEventListener.java:289)
	at org.hibernate.event.internal.AbstractSaveEventListener.performSave(AbstractSaveEventListener.java:195)
	at org.hibernate.event.internal.AbstractSaveEventListener.saveWithGeneratedId(AbstractSaveEventListener.java:126)
	at org.hibernate.event.internal.DefaultSaveOrUpdateEventListener.saveWithGeneratedOrRequestedId(DefaultSaveOrUpdateEventListener.java:209)
	at org.hibernate.event.internal.DefaultSaveOrUpdateEventListener.entityIsTransient(DefaultSaveOrUpdateEventListener.java:194)
	at org.hibernate.event.internal.DefaultSaveOrUpdateEventListener.performSaveOrUpdate(DefaultSaveOrUpdateEventListener.java:114)
	at org.hibernate.event.internal.DefaultSaveOrUpdateEventListener.onSaveOrUpdate(DefaultSaveOrUpdateEventListener.java:90)
	at org.grails.orm.hibernate.support.ClosureEventTriggeringInterceptor.onSaveOrUpdate(ClosureEventTriggeringInterceptor.java:113)
	at org.hibernate.internal.SessionImpl.fireSaveOrUpdate(SessionImpl.java:684)
	at org.hibernate.internal.SessionImpl.saveOrUpdate(SessionImpl.java:676)
	at org.hibernate.engine.spi.CascadingActions$5.cascade(CascadingActions.java:235)
	at org.hibernate.engine.internal.Cascade.cascadeToOne(Cascade.java:350)
	at org.hibernate.engine.internal.Cascade.cascadeAssociation(Cascade.java:293)
	at org.hibernate.engine.internal.Cascade.cascadeProperty(Cascade.java:161)
	at org.hibernate.engine.internal.Cascade.cascade(Cascade.java:118)
	at org.hibernate.event.internal.AbstractSaveEventListener.cascadeAfterSave(AbstractSaveEventListener.java:470)
	at org.hibernate.event.internal.AbstractSaveEventListener.performSaveOrReplicate(AbstractSaveEventListener.java:295)
	at org.hibernate.event.internal.AbstractSaveEventListener.performSave(AbstractSaveEventListener.java:195)
	at org.hibernate.event.internal.AbstractSaveEventListener.saveWithGeneratedId(AbstractSaveEventListener.java:126)
	at org.hibernate.event.internal.DefaultSaveOrUpdateEventListener.saveWithGeneratedOrRequestedId(DefaultSaveOrUpdateEventListener.java:209)
	at org.hibernate.event.internal.DefaultSaveOrUpdateEventListener.entityIsTransient(DefaultSaveOrUpdateEventListener.java:194)
	at org.hibernate.event.internal.DefaultSaveOrUpdateEventListener.performSaveOrUpdate(DefaultSaveOrUpdateEventListener.java:114)
	at org.hibernate.event.internal.DefaultSaveOrUpdateEventListener.onSaveOrUpdate(DefaultSaveOrUpdateEventListener.java:90)
	at org.grails.orm.hibernate.support.ClosureEventTriggeringInterceptor.onSaveOrUpdate(ClosureEventTriggeringInterceptor.java:113)
	at org.hibernate.internal.SessionImpl.fireSaveOrUpdate(SessionImpl.java:684)
	at org.hibernate.internal.SessionImpl.saveOrUpdate(SessionImpl.java:676)
	at org.hibernate.engine.spi.CascadingActions$5.cascade(CascadingActions.java:235)
	at org.hibernate.engine.internal.Cascade.cascadeToOne(Cascade.java:350)
	at org.hibernate.engine.internal.Cascade.cascadeAssociation(Cascade.java:293)
	at org.hibernate.engine.internal.Cascade.cascadeProperty(Cascade.java:161)
	at org.hibernate.engine.internal.Cascade.cascadeCollectionElements(Cascade.java:379)
	at org.hibernate.engine.internal.Cascade.cascadeCollection(Cascade.java:319)
	at org.hibernate.engine.internal.Cascade.cascadeAssociation(Cascade.java:296)
	at org.hibernate.engine.internal.Cascade.cascadeProperty(Cascade.java:161)
	at org.hibernate.engine.internal.Cascade.cascade(Cascade.java:118)
	at org.hibernate.event.internal.AbstractSaveEventListener.cascadeAfterSave(AbstractSaveEventListener.java:470)
	at org.hibernate.event.internal.AbstractSaveEventListener.performSaveOrReplicate(AbstractSaveEventListener.java:295)
	at org.hibernate.event.internal.AbstractSaveEventListener.performSave(AbstractSaveEventListener.java:195)
	at org.hibernate.event.internal.AbstractSaveEventListener.saveWithGeneratedId(AbstractSaveEventListener.java:126)
	at org.hibernate.event.internal.DefaultSaveOrUpdateEventListener.saveWithGeneratedOrRequestedId(DefaultSaveOrUpdateEventListener.java:209)
	at org.hibernate.event.internal.DefaultSaveOrUpdateEventListener.entityIsTransient(DefaultSaveOrUpdateEventListener.java:194)
	at org.hibernate.event.internal.DefaultSaveOrUpdateEventListener.performSaveOrUpdate(DefaultSaveOrUpdateEventListener.java:114)
	at org.hibernate.event.internal.DefaultSaveOrUpdateEventListener.onSaveOrUpdate(DefaultSaveOrUpdateEventListener.java:90)
	at org.grails.orm.hibernate.support.ClosureEventTriggeringInterceptor.onSaveOrUpdate(ClosureEventTriggeringInterceptor.java:113)
	at org.hibernate.internal.SessionImpl.fireSaveOrUpdate(SessionImpl.java:684)
	at org.hibernate.internal.SessionImpl.saveOrUpdate(SessionImpl.java:676)
	at org.hibernate.internal.SessionImpl.saveOrUpdate(SessionImpl.java:671)
	at org.grails.orm.hibernate.AbstractHibernateGormInstanceApi.performSave_closure3(AbstractHibernateGormInstanceApi.groovy:255)
	at groovy.lang.Closure.call(Closure.java:426)
	at org.grails.orm.hibernate.GrailsHibernateTemplate.doExecute(GrailsHibernateTemplate.java:198)
	at org.grails.orm.hibernate.GrailsHibernateTemplate.execute(GrailsHibernateTemplate.java:142)
	at org.grails.orm.hibernate.GrailsHibernateTemplate.execute(GrailsHibernateTemplate.java:112)
	at org.grails.orm.hibernate.AbstractHibernateGormInstanceApi.performSave(AbstractHibernateGormInstanceApi.groovy:254)
	at org.grails.orm.hibernate.AbstractHibernateGormInstanceApi.save(AbstractHibernateGormInstanceApi.groovy:179)
	at org.grails.datastore.gorm.GormEntity$Trait$Helper.save(GormEntity.groovy:165)
	at baseball.test.BiDirectionalAssociationSpecSpec.$tt__setupData(BiDirectionalAssociationSpecSpec.groovy:42)
	at baseball.test.BiDirectionalAssociationSpecSpec.setupData_closure1(BiDirectionalAssociationSpecSpec.groovy)
	at groovy.lang.Closure.call(Closure.java:426)
	at groovy.lang.Closure.call(Closure.java:442)
	at grails.transaction.GrailsTransactionTemplate$1.doInTransaction(GrailsTransactionTemplate.groovy:67)
	at org.springframework.transaction.support.TransactionTemplate.execute(TransactionTemplate.java:133)
	at grails.transaction.GrailsTransactionTemplate.executeAndRollback(GrailsTransactionTemplate.groovy:64)
	at baseball.test.BiDirectionalAssociationSpecSpec.$tt__$spock_feature_0_0(BiDirectionalAssociationSpecSpec.groovy:20)
	at baseball.test.BiDirectionalAssociationSpecSpec.a valid team is bootstrapped into the test_closure2(BiDirectionalAssociationSpecSpec.groovy)
	at groovy.lang.Closure.call(Closure.java:426)
	at groovy.lang.Closure.call(Closure.java:442)
	at grails.transaction.GrailsTransactionTemplate$1.doInTransaction(GrailsTransactionTemplate.groovy:67)
	at org.springframework.transaction.support.TransactionTemplate.execute(TransactionTemplate.java:133)
	at grails.transaction.GrailsTransactionTemplate.executeAndRollback(GrailsTransactionTemplate.groovy:64)
	at baseball.test.BiDirectionalAssociationSpecSpec.a valid team is bootstrapped into the test(BiDirectionalAssociationSpecSpec
```
