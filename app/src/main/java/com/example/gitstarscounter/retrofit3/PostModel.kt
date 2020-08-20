package com.example.gitstarscounter.retrofit3

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class PostModel {

    /**
     * @param site The site
     */
    @SerializedName("site")
    @Expose
    var site: String? = null
    /**
     * @return Site name
     */
    /**
     * @param name Site name
     */
    @SerializedName("name")
    @Expose
    var name: String? = null
    /**
     * @return Site description
     */
    /**
     * @param desc Site description
     */
    @SerializedName("desc")
    @Expose
    var desc: String? = null
    /**
     * @return The link
     */
    /**
     * @param link The link
     */
    @SerializedName("link")
    @Expose
    var link: String? = null
    /**
     * @return The elementPureHtml
     */
    /**
     * @param elementPureHtml The elementPureHtml
     */
    @SerializedName("elementPureHtml")
    @Expose
    var elementPureHtml: String? = null
}