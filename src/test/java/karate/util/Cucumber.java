package karate.util;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Base64;

import java.nio.file.*;

public class Cucumber {
    
    public String output(String path) {
        Collection<File> jsonFiles = FileUtils.listFiles(new File(path), new String[] { "json" }, true);
        List<String> jsonPaths = new ArrayList<>(jsonFiles.size());
        jsonFiles.forEach(file -> jsonPaths.add(file.getAbsolutePath()));
        return join(jsonPaths);
    }

    private String join(List<String> jsonPath) {
        List<JSONArray> data = new ArrayList<>();

        jsonPath.forEach(path -> {
            try {
                String content = new String(Files.readAllBytes(Paths.get(path)));
                data.add(new JSONArray(content));
            } catch (Exception e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        });

        JSONArray cucumberjson = new JSONArray();

        data.forEach(json -> json.forEach(o -> cucumberjson.put(o)));

        cucumberjson.forEach(item -> {
            JSONObject scenario = (JSONObject) item;
            JSONArray elements = scenario.getJSONArray("elements");

            elements.forEach(elementItem -> {
                JSONObject element = (JSONObject) elementItem;
                JSONArray steps = element.getJSONArray("steps");

                steps.forEach(stepItem -> {
                    JSONObject step = (JSONObject) stepItem;
                    if (step.has("doc_string")) {
                        JSONObject docString = step.getJSONObject("doc_string");
                        JSONObject embeddings = new JSONObject();

                        embeddings.put("mime_type", "text/plain");
                        String value = docString.getString("value");
                        String base64Value = Base64.getEncoder().encodeToString(value.getBytes());
                        embeddings.put("data", base64Value);

                        JSONArray embeddingsArray;
                        if (step.has("embeddings")) {
                            embeddingsArray = step.getJSONArray("embeddings");
                        } else {
                            embeddingsArray = new JSONArray();
                            step.put("embeddings", embeddingsArray);
                        }
                        embeddingsArray.put(embeddings);
                    }
                });
            });
        });

        compact(cucumberjson);
        return cucumberjson.toString();
    }
 
    private void compact(JSONArray jsonArray) {
        try {
            jsonArray.forEach(item -> {
                JSONObject feature = (JSONObject) item;
                JSONArray elements = feature.getJSONArray("elements");

                for (int j = 0; j < elements.length(); j++) {
                    JSONObject element = elements.getJSONObject(j);
                    if (element.getString("type").equals("background")) {
                        elements.remove(j);
                        j--; 
                    }
                }

                elements.forEach(elementItem -> {
                    JSONObject element = (JSONObject) elementItem;
                    JSONArray steps = element.getJSONArray("steps");

                    JSONObject firstStep = steps.getJSONObject(0);
                    String keywordStep = firstStep.getString("keyword");
                    JSONObject matchStep = firstStep.getJSONObject("match");
                    int lineStep = firstStep.getInt("line");

                    StringBuilder nameStep = new StringBuilder();
                    long durationStep = 0;
                    String statusStep = "passed";
                    JSONObject docString = null;
                    JSONArray embeddings = null;

                    for (int i = 0; i < steps.length(); i++) {
                        JSONObject step = steps.getJSONObject(i);
                        String _name = step.getString("name");
                        long _duration = step.getJSONObject("result").getLong("duration");
                        String _status = step.getJSONObject("result").getString("status");
                        String _keyword = step.getString("keyword");

                        if (i > 0) {
                            nameStep.append(_keyword + " ");
                        }

                        nameStep.append(_name);
                        durationStep += _duration;

                        if (_status.equals("failed")) {
                            statusStep = "failed";
                        }

                        if (i != steps.length() - 1) {
                            nameStep.append("\n");
                        }

                        if (step.has("doc_string")) {
                            docString = step.getJSONObject("doc_string");
                        }

                        if (step.has("embeddings")) {
                            embeddings = step.getJSONArray("embeddings");
                        }
                    }

                    JSONObject combinedStep = new JSONObject();
                    combinedStep.put("keyword", keywordStep);
                    combinedStep.put("match", matchStep);
                    combinedStep.put("line", lineStep);
                    combinedStep.put("name", nameStep.toString().trim());

                    JSONObject resultStep = new JSONObject();
                    resultStep.put("duration", durationStep);
                    resultStep.put("status", statusStep);
                    combinedStep.put("result", resultStep);
                    combinedStep.put("doc_string", docString);
                    combinedStep.put("embeddings", embeddings);

                    JSONArray newSteps = new JSONArray();
                    newSteps.put(combinedStep);
                    element.put("steps", newSteps);
                });
            });

            String ruta = System.getProperty("user.dir") + "/target/karate-reports/cucumber.json";
            FileWriter writer = new FileWriter(ruta);
            writer.write(jsonArray.toString(4));
            writer.close();
            System.out.println("[SUCCESS] Build Cucumber JSON: " + ruta);
            System.out.println("===================================================================");
        } catch (IOException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }
}
