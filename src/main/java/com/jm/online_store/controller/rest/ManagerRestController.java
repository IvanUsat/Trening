package com.jm.online_store.controller.rest;

import com.jm.online_store.exception.OrdersNotFoundException;
import com.jm.online_store.model.News;
import com.jm.online_store.model.Product;
import com.jm.online_store.model.dto.SalesReportDto;
import com.jm.online_store.service.interf.NewsService;
import com.jm.online_store.service.interf.OrderService;
import com.jm.online_store.service.interf.ProductService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Рест контроллер для управления новостями из кабинете менеджера, а также публикации новостей
 * на странице новостей
 */
@AllArgsConstructor
@RestController
@RequestMapping(value = "api/manager")
@Slf4j
public class ManagerRestController {

    private final NewsService newsService;
    private final OrderService orderService;
    private final ProductService productService;

    /**
     * Метод возвращающий всписок всех новостей
     *
     * @return List<News> возвращает список всех новстей из базы данных
     */
    @GetMapping("/news")
    public ResponseEntity<List<News>> allNews() {
        return ResponseEntity.ok().body(newsService.findAll());
    }

    /**
     * Метод сохраняет новости в базу данных
     *
     * @param news сущность для сохранения в базе данных
     * @return возвращает заполненную сущность клиенту
     */
    @PostMapping("/news/post")
    public ResponseEntity<News> newsPost(@RequestBody News news) {

        if (news.getPostingDate() == null || news.getPostingDate().isBefore(LocalDate.now())) {
            news.setPostingDate(LocalDate.now());
        }
        newsService.save(news);
        return ResponseEntity.ok().body(news);
    }

    /**
     * Метод обновляет сущность в базе данных
     *
     * @param news сущность для сохранения в базе данных
     * @return возвращает обновленную сущность клиенту
     */
    @PutMapping("/news/update")
    public ResponseEntity<News> newsUpdate(@RequestBody News news) {

        if (news.getPostingDate() == null || news.getPostingDate().isBefore(LocalDate.now())) {
            news.setPostingDate(LocalDate.now());
        }
        newsService.save(news);
        return ResponseEntity.ok().body(news);
    }

    /**
     * Метод удаляет сушность из базы данных по уникальному идентификатору
     *
     * @param id уникальный идентификатор
     * @return возвращает идентификатор удаленной сущности клиенту
     */
    @DeleteMapping("/news/{id}/delete")
    public ResponseEntity<Long> newsDelete(@PathVariable Long id) {
        newsService.deleteById(id);
        return ResponseEntity.ok().body(id);
    }

    /**
     * Get mapping for get request to response with sales during the custom date range
     *
     * @param stringStartDate - start of custom date range
     * @param stringEndDate   - end of custom date range
     * @return - {@link ResponseEntity} with list of Orders with status complete
     */
    @GetMapping("/sales")
    public ResponseEntity<List<SalesReportDto>> getSalesForCustomRange(@RequestParam String stringStartDate, @RequestParam String stringEndDate) {
        LocalDate startDate = LocalDate.parse(stringStartDate);
        LocalDate endDate = LocalDate.parse(stringEndDate);
        try {
            return ResponseEntity.ok(orderService.findAllSalesBetween(startDate, endDate));
        } catch (OrdersNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Mapping for csv export.
     *
     * @param stringStartDate - beginning of the period that receives from frontend in as String
     * @param stringEndDate   - end of the period that receives from frontend in as String
     * @param response        - response to write back stream with csv
     * @return - ResponseEntity
     */
    @GetMapping("/sales/exportCSV")
    public ResponseEntity<FileSystemResource> getSalesForCustomRangeAndExportToCSV(@RequestParam String stringStartDate, @RequestParam String stringEndDate, HttpServletResponse response) {
        LocalDate startDate = LocalDate.parse(stringStartDate);
        LocalDate endDate = LocalDate.parse(stringEndDate);
        try {
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            StatefulBeanToCsv<SalesReportDto> writer = new StatefulBeanToCsvBuilder<SalesReportDto>(response.getWriter())
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(';')
                    .withOrderedResults(true)
                    .build();
            writer.write(orderService.findAllSalesBetween(startDate, endDate));
            return ResponseEntity.ok().build();
        } catch (OrdersNotFoundException e) {
            log.debug("csv file was successfully sent");
            return ResponseEntity.notFound().build();
        } catch (CsvRequiredFieldEmptyException | IOException | CsvDataTypeMismatchException e) {
            log.debug("Problem with writing csv file");
            return ResponseEntity.badRequest().build();
        }
    }
}
