package zw.co.hisolutions.pos.dayend.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zw.co.hisolutions.pos.dayend.entity.DayEnd;
import zw.co.hisolutions.pos.dayend.entity.DayEndItem;
import zw.co.hisolutions.pos.dayend.entity.dao.DayEndDao;
import zw.co.hisolutions.pos.dayend.entity.dao.DayEndItemDao;
import zw.co.hisolutions.pos.dayend.service.DayEndService;

/**
 *
 * @author kinah
 */
@Service
public class DayEndServiceImpl implements DayEndService {

    @Autowired
    DayEndDao dayEndDao;

    @Autowired
    DayEndItemDao dayEndItemDao;

    @Override
    public List<DayEnd> getAllBySearch(DayEnd dayEnd) {
        if (dayEnd.getModifiedByUser() != null && !dayEnd.getModifiedByUser().isEmpty()) {
            return dayEndDao.findByModifiedByUserContainingIgnoreCaseAndDayEndDateBetweenOrderByIdDesc(dayEnd.getModifiedByUser(), dayEnd.getDayStartDateTime(), dayEnd.getDayEndDate());
        }
        return dayEndDao.findByDayEndDateBetweenOrderByIdDesc(dayEnd.getDayStartDateTime(), dayEnd.getDayEndDate());
    }

    @Override
    public DayEnd getByID(Long id) {
        DayEnd dayEnd = dayEndDao.getOne(id);
        dayEnd = dayEnd == null ? new DayEnd() : dayEnd;
        return dayEnd;
    }

    @Override
    public DayEnd save(DayEnd dayEnd) {
        if (dayEnd.getDayEndItems() == null || dayEnd.getDayEndItems().isEmpty()) {
            return null;
        }

        dayEnd.getDayEndItems().stream().forEach(line -> {
            line.setDayEnd(dayEnd);
        });

        return dayEndDao.save(dayEnd);
    }

    @Override
    public List<DayEndItem> getByDayEndId(Long id) {
        return dayEndItemDao.findByDayEndId(id);
    }

    @Override
    public List<DayEnd> findAll() {
        return dayEndDao.findAll();
    }

}
