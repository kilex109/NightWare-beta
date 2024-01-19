package nightware.main.command.impl.report;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class ReportSender {
    private final String url;
    private String content;
    private String username;
    private String avatarUrl;
    private boolean tts;
    private List<EmbedObject> embeds = new ArrayList();

    public ReportSender(String url) {
        this.url = url;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setTts(boolean tts) {
        this.tts = tts;
    }

    public void addEmbed(ReportSender.EmbedObject embed) {
        this.embeds.add(embed);
    }

    public void execute() {
        if (this.content == null && this.embeds.isEmpty()) {
            throw new IllegalArgumentException("Set content or add at least one EmbedObject");
        } else {
            ReportSender.JSONObject json = new ReportSender.JSONObject();
            json.put("content", this.content);
            json.put("username", this.username);
            json.put("avatar_url", this.avatarUrl);
            json.put("tts", this.tts);
            if (!this.embeds.isEmpty()) {
                List<ReportSender.JSONObject> embedObjects = new ArrayList();
                Iterator var3 = this.embeds.iterator();

                while(var3.hasNext()) {
                    ReportSender.EmbedObject embed = (ReportSender.EmbedObject)var3.next();
                    ReportSender.JSONObject jsonEmbed = new ReportSender.JSONObject();
                    jsonEmbed.put("title", embed.getTitle());
                    jsonEmbed.put("description", embed.getDescription());
                    jsonEmbed.put("url", embed.getUrl());
                    if (embed.getColor() != null) {
                        Color color = embed.getColor();
                        int rgb = color.getRed();
                        rgb = (rgb << 8) + color.getGreen();
                        rgb = (rgb << 8) + color.getBlue();
                        jsonEmbed.put("color", rgb);
                    }

                    ReportSender.EmbedObject.Footer footer = embed.getFooter();
                    ReportSender.EmbedObject.Image image = embed.getImage();
                    ReportSender.EmbedObject.Thumbnail thumbnail = embed.getThumbnail();
                    ReportSender.EmbedObject.Author author = embed.getAuthor();
                    List<ReportSender.EmbedObject.Field> fields = embed.getFields();
                    ReportSender.JSONObject jsonAuthor;
                    if (footer != null) {
                        jsonAuthor = new ReportSender.JSONObject();
                        jsonAuthor.put("text", footer.getText());
                        jsonAuthor.put("icon_url", footer.getIconUrl());
                        jsonEmbed.put("footer", jsonAuthor);
                    }

                    if (image != null) {
                        jsonAuthor = new ReportSender.JSONObject();
                        jsonAuthor.put("url", image.getUrl());
                        jsonEmbed.put("image", jsonAuthor);
                    }

                    if (thumbnail != null) {
                        jsonAuthor = new ReportSender.JSONObject();
                        jsonAuthor.put("url", thumbnail.getUrl());
                        jsonEmbed.put("thumbnail", jsonAuthor);
                    }

                    if (author != null) {
                        jsonAuthor = new ReportSender.JSONObject();
                        jsonAuthor.put("name", author.getName());
                        jsonAuthor.put("url", author.getUrl());
                        jsonAuthor.put("icon_url", author.getIconUrl());
                        jsonEmbed.put("author", jsonAuthor);
                    }

                    List<ReportSender.JSONObject> jsonFields = new ArrayList();
                    Iterator var12 = fields.iterator();

                    while(var12.hasNext()) {
                        ReportSender.EmbedObject.Field field = (ReportSender.EmbedObject.Field)var12.next();
                        ReportSender.JSONObject jsonField = new ReportSender.JSONObject();
                        jsonField.put("name", field.getName());
                        jsonField.put("value", field.getValue());
                        jsonField.put("inline", field.isInline());
                        jsonFields.add(jsonField);
                    }

                    jsonEmbed.put("fields", jsonFields.toArray());
                    embedObjects.add(jsonEmbed);
                }

                json.put("embeds", embedObjects.toArray());
            }

            URL url = null;

            try {
                url = new URL(this.url);
            } catch (MalformedURLException var22) {
            }

            HttpsURLConnection connection = null;

            try {
                connection = (HttpsURLConnection)url.openConnection();
            } catch (IOException var21) {
            }

            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_");
            connection.setDoOutput(true);

            try {
                connection.setRequestMethod("POST");
            } catch (ProtocolException var20) {
            }

            OutputStream stream = null;

            try {
                stream = connection.getOutputStream();
            } catch (IOException var19) {
            }

            try {
                stream.write(json.toString().getBytes());
            } catch (IOException var18) {
            }

            try {
                stream.flush();
            } catch (IOException var17) {
            }

            try {
                stream.close();
            } catch (IOException var16) {
            }

            try {
                connection.getInputStream().close();
            } catch (IOException var15) {
            }

            connection.disconnect();
        }
    }

    private class JSONObject {
        private final HashMap<String, Object> map = new HashMap();

        void put(String key, Object value) {
            if (value != null) {
                this.map.put(key, value);
            }

        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            Set<Map.Entry<String, Object>> entrySet = this.map.entrySet();
            builder.append("{");
            int i = 0;
            Iterator var4 = entrySet.iterator();

            while(var4.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry)var4.next();
                Object val = entry.getValue();
                builder.append(this.quote((String)entry.getKey())).append(":");
                if (val instanceof String) {
                    builder.append(this.quote(String.valueOf(val)));
                } else if (val instanceof Integer) {
                    builder.append(Integer.valueOf(String.valueOf(val)));
                } else if (val instanceof Boolean) {
                    builder.append(val);
                } else if (val instanceof ReportSender.JSONObject) {
                    builder.append(val.toString());
                } else if (val.getClass().isArray()) {
                    builder.append("[");
                    int len = Array.getLength(val);

                    for(int j = 0; j < len; ++j) {
                        builder.append(Array.get(val, j).toString()).append(j != len - 1 ? "," : "");
                    }

                    builder.append("]");
                }

                ++i;
                builder.append(i == entrySet.size() ? "}" : ",");
            }

            return builder.toString();
        }

        private String quote(String string) {
            return "\"" + string + "\"";
        }
    }

    public static class EmbedObject {
        private String title;
        private String description;
        private String url;
        private Color color;
        private ReportSender.EmbedObject.Footer footer;
        private ReportSender.EmbedObject.Thumbnail thumbnail;
        private ReportSender.EmbedObject.Image image;
        private ReportSender.EmbedObject.Author author;
        private List<ReportSender.EmbedObject.Field> fields = new ArrayList();

        public String getTitle() {
            return this.title;
        }

        public String getDescription() {
            return this.description;
        }

        public String getUrl() {
            return this.url;
        }

        public Color getColor() {
            return this.color;
        }

        public ReportSender.EmbedObject.Footer getFooter() {
            return this.footer;
        }

        public ReportSender.EmbedObject.Thumbnail getThumbnail() {
            return this.thumbnail;
        }

        public ReportSender.EmbedObject.Image getImage() {
            return this.image;
        }

        public ReportSender.EmbedObject.Author getAuthor() {
            return this.author;
        }

        public List<ReportSender.EmbedObject.Field> getFields() {
            return this.fields;
        }

        public ReportSender.EmbedObject setTitle(String title) {
            this.title = title;
            return this;
        }

        public ReportSender.EmbedObject setDescription(String description) {
            this.description = description;
            return this;
        }

        public ReportSender.EmbedObject setUrl(String url) {
            this.url = url;
            return this;
        }

        public ReportSender.EmbedObject setColor(Color color) {
            this.color = color;
            return this;
        }

        public ReportSender.EmbedObject setFooter(String text, String icon) {
            this.footer = new ReportSender.EmbedObject.Footer(text, icon);
            return this;
        }

        public ReportSender.EmbedObject setThumbnail(String url) {
            this.thumbnail = new ReportSender.EmbedObject.Thumbnail(url);
            return this;
        }

        public ReportSender.EmbedObject setImage(String url) {
            this.image = new ReportSender.EmbedObject.Image(url);
            return this;
        }

        public ReportSender.EmbedObject setAuthor(String name, String url, String icon) {
            this.author = new ReportSender.EmbedObject.Author(name, url, icon);
            return this;
        }

        public ReportSender.EmbedObject addField(String name, String value, boolean inline) {
            this.fields.add(new ReportSender.EmbedObject.Field(name, value, inline));
            return this;
        }

        private class Footer {
            private String text;
            private String iconUrl;

            private Footer(String text, String iconUrl) {
                this.text = text;
                this.iconUrl = iconUrl;
            }

            private String getText() {
                return this.text;
            }

            private String getIconUrl() {
                return this.iconUrl;
            }
        }

        private class Thumbnail {
            private String url;

            private Thumbnail(String url) {
                this.url = url;
            }

            private String getUrl() {
                return this.url;
            }
        }

        private class Image {
            private String url;

            private Image(String url) {
                this.url = url;
            }

            private String getUrl() {
                return this.url;
            }
        }

        private class Author {
            private String name;
            private String url;
            private String iconUrl;

            private Author(String name, String url, String iconUrl) {
                this.name = name;
                this.url = url;
                this.iconUrl = iconUrl;
            }

            private String getName() {
                return this.name;
            }

            private String getUrl() {
                return this.url;
            }

            private String getIconUrl() {
                return this.iconUrl;
            }
        }

        private class Field {
            private String name;
            private String value;
            private boolean inline;

            private Field(String name, String value, boolean inline) {
                this.name = name;
                this.value = value;
                this.inline = inline;
            }

            private String getName() {
                return this.name;
            }

            private String getValue() {
                return this.value;
            }

            private boolean isInline() {
                return this.inline;
            }
        }
    }
}
