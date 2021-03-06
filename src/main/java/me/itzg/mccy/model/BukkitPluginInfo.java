package me.itzg.mccy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;

/**
 * A partial POJO compliance of http://wiki.bukkit.org/Plugin_YAML
 * @author Geoff Bourne
 * @since 0.1
 */
@JsonIgnoreProperties({"commands", "permissions"})
public class BukkitPluginInfo {
    private String name;
    private String main;
    private Collection<String> authors;
    private String version;
    private Collection<String> softdepend;
    private String description;
    private String website;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<String> getAuthors() {
        return authors;
    }

    public void setAuthors(Collection<String> authors) {
        this.authors = authors;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Collection<String> getSoftdepend() {
        return softdepend;
    }

    public void setSoftdepend(Collection<String> softdepend) {
        this.softdepend = softdepend;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public Collection<String> getAuthor() {
        return authors;
    }

    public void setAuthor(Collection<String> author) {
        this.authors = author;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
