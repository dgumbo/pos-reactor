import { ReportsAdminModule } from './reports-admin.module';

describe('ReportsAdminModule', () => {
  let reportsAdminModule: ReportsAdminModule;

  beforeEach(() => {
    reportsAdminModule = new ReportsAdminModule();
  });

  it('should create an instance', () => {
    expect(reportsAdminModule).toBeTruthy();
  });
});
