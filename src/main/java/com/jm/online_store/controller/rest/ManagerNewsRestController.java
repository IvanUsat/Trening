package com.jm.online_store.controller.rest;

import com.jm.online_store.exception.NewsNotFoundException;
import com.jm.online_store.model.News;
import com.jm.online_store.model.dto.NewsFilterDto;
import com.jm.online_store.service.interf.NewsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Рест контроллер для управления новостями из кабинете менеджера, а также публикации новостей
 * на странице новостей
 */
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/manager/news")
public class ManagerNewsRestController {

    private final NewsService newsService;

    /**
     * Mapping accepts @PathVariable {@link Long} id
     *
     * @param id - {@link Long} id of news entity
     * @return {@link ResponseEntity<News>} or ResponseEntity.notFound()
     */
    @GetMapping("/{id}")
    public ResponseEntity<News> getNews(@PathVariable Long id) {
        News news;
        try {
            news = newsService.findById(id);
        } catch (NewsNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(news);
    }

    /**
     * Method returns all news
     *
     * @return List<News> возвращает список всех новостей из базы данных
     */
    @GetMapping("/all")
    public ResponseEntity<List<News>> allNews() {
        List<News> allNewsList;
        try {
            allNewsList = newsService.findAll();
            return ResponseEntity.ok(allNewsList);
        } catch (NewsNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Метод возвращает страницу новостей
     *
     * @param page параметры страницы
     * @return Page<News> возвращает страницу новостей
     */
    @GetMapping("/page")
    public ResponseEntity<Page<News>> getPage(@PageableDefault Pageable page, NewsFilterDto filterDto) {
        Page<News> response = newsService.findAll(page, filterDto);
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Method returns published news
     *
     * @return - ResponseEntity<List<News>>
     */
    @GetMapping("/published")
    public ResponseEntity<List<News>> getPublishedNews() {
        try {
            List<News> publishedNews = newsService.getAllPublished();
            return ResponseEntity.ok(publishedNews);
        } catch (NewsNotFoundException e) {
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * Method returns unpublished news
     *
     * @return - ResponseEntity<List<News>>
     */
    @GetMapping("/unpublished")
    public ResponseEntity<List<News>> getUnpublishedNews() {
        try {
            List<News> unpublishedNews = newsService.getAllUnpublished();
            return ResponseEntity.ok(unpublishedNews);
        } catch (NewsNotFoundException e) {
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * Method returns archived news
     *
     * @return - ResponseEntity<List<News>>
     */
    @GetMapping("/archived")
    public ResponseEntity<List<News>> getArchivedNews() {
        try {
            List<News> archived = newsService.getAllArchivedNews();
            return ResponseEntity.ok(archived);
        } catch (NewsNotFoundException e) {
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * Метод сохраняет новости в базу данных
     *
     * @param news сущность для сохранения в базе данных
     * @return возвращает заполненную сущность клиенту
     */
    @PostMapping
    public ResponseEntity<News> newsPost(@RequestBody News news) {
        newsService.save(news);
        return ResponseEntity.ok(news);
    }

    /**
     * Метод обновляет сущность в базе данных
     *
     * @param news сущность для сохранения в базе данных
     * @return возвращает обновленную сущность клиенту
     */
    @PutMapping
    public ResponseEntity<News> newsUpdate(@RequestBody News news) {
        newsService.update(news);
        return ResponseEntity.ok(news);
    }

    /**
     * Метод удаляет сушность из базы данных по уникальному идентификатору
     *
     * @param id уникальный идентификатор
     * @return возвращает идентификатор удаленной сущности клиенту
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> newsDelete(@PathVariable Long id) {
        try {
            newsService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (NewsNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
