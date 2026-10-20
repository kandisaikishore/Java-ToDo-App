package demo.todo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class ApiApplication {
 public static void main(String[] args){SpringApplication.run(ApiApplication.class,args);}
 @RestController @RequestMapping("/api/tasks") @CrossOrigin(origins="*")
 static class TasksApi {
  private final Map<Integer,Map<String,Object>> tasks=new LinkedHashMap<>();private final AtomicInteger ids=new AtomicInteger();
  @GetMapping List<Map<String,Object>> all(){return new ArrayList<>(tasks.values());}
  @PostMapping ResponseEntity<?> create(@RequestBody Map<String,Object> body){String title=String.valueOf(body.getOrDefault("title","")).trim();if(title.isEmpty())return ResponseEntity.badRequest().body(Map.of("error","title is required"));int id=ids.incrementAndGet();Map<String,Object>t=new LinkedHashMap<>();t.put("id",id);t.put("title",title);t.put("priority",body.getOrDefault("priority","Medium"));t.put("due",body.getOrDefault("due",""));t.put("done",false);t.put("created",java.time.LocalDate.now().toString());tasks.put(id,t);return ResponseEntity.status(201).body(t);}
  @PatchMapping("/{id}") ResponseEntity<?> update(@PathVariable int id,@RequestBody Map<String,Object> body){Map<String,Object>t=tasks.get(id);if(t==null)return ResponseEntity.notFound().build();if(body.containsKey("done"))t.put("done",Boolean.valueOf(String.valueOf(body.get("done"))));if(body.containsKey("title"))t.put("title",body.get("title"));return ResponseEntity.ok(t);}
  @PutMapping("/sync") ResponseEntity<?> sync(@RequestBody List<Map<String,Object>> incoming){tasks.clear();int max=0;for(Map<String,Object> item:incoming){Object raw=item.get("id");int id=raw instanceof Number n?n.intValue():ids.incrementAndGet();if(id<1)id=ids.incrementAndGet();Map<String,Object>t=new LinkedHashMap<>();t.put("id",id);t.put("title",String.valueOf(item.getOrDefault("title","Untitled")));t.put("priority",item.getOrDefault("priority","Medium"));t.put("due",item.getOrDefault("due",""));t.put("done",Boolean.TRUE.equals(item.get("done")));t.put("created",item.getOrDefault("created",java.time.LocalDate.now().toString()));tasks.put(id,t);max=Math.max(max,id);}ids.set(Math.max(ids.get(),max));return ResponseEntity.ok(new ArrayList<>(tasks.values()));}
  @DeleteMapping("/{id}") ResponseEntity<?> delete(@PathVariable int id){return tasks.remove(id)==null?ResponseEntity.notFound().build():ResponseEntity.noContent().build();}
  @DeleteMapping ResponseEntity<?> clearDone(){tasks.values().removeIf(t->Boolean.TRUE.equals(t.get("done")));return ResponseEntity.noContent().build();}
 }
}